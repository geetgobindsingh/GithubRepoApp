package com.geetgobindingh.githubrepoapp.repository.trending

import androidx.sqlite.db.SimpleSQLiteQuery
import com.geetgobindingh.githubrepoapp.data.DataManager
import com.geetgobindingh.githubrepoapp.data.db.model.githubrepository.GithubRepository
import com.geetgobindingh.githubrepoapp.data.mapper.githubrepository.GithubRepositoryMapper
import io.reactivex.Observable
import javax.inject.Inject

class TrendingRepositoryImpl @Inject constructor(
    private val dataManager: DataManager,
    private val githubRepositoryMapper: GithubRepositoryMapper
) : TrendingRepository {

    sealed class SortingOrder {
        object ByStars : SortingOrder()
        object ByNames : SortingOrder()
        object Default : SortingOrder()
    }

    data class RepositoriesQueryParams(
        var forceFetchFromRemote: Boolean = false,
        var sortBy: SortingOrder = SortingOrder.Default
    )

    override fun getTrendingRepositories(queryParams: RepositoriesQueryParams): Observable<List<GithubRepository>> {
        if (queryParams.forceFetchFromRemote == false
            && dataManager.isCached()
            && !dataManager.isExpired()
        ) {
            return Observable.fromCallable<List<GithubRepository>> {
                dataManager.getDataBaseHelper()
                    .githubRepositoryDao()
                    .getAllRepositories(SimpleSQLiteQuery(getQueryString(queryParams)))
            }
        } else {
            return dataManager
                .getNetworkBaseHelper()
                .getGithubRepositoryService()
                .fetchTrendingRepositories()
                .flatMap { apiResponse ->
                    Observable.fromIterable(apiResponse.items)
                        .map { apiGithubRepository ->
                            githubRepositoryMapper.map(apiGithubRepository)
                        }.toList().toObservable()
                }.map {
                    dataManager.clearDb()
                    it
                }
                .flatMap {
                    saveGithubRepositoryList(it)
                }.flatMap {
                    Observable.just(
                        dataManager.getDataBaseHelper()
                            .githubRepositoryDao()
                            .getAllRepositories(SimpleSQLiteQuery(getQueryString(queryParams)))
                    )
                }.map {
                    dataManager.setLastCacheTime(System.currentTimeMillis())
                    it
                }
        }
    }

    private fun getQueryString(queryParams: RepositoriesQueryParams): String {
        return when (queryParams.sortBy) {
            SortingOrder.ByStars -> {
                "SELECT * from github_repository order by stars"
            }
            SortingOrder.ByNames -> {
                "SELECT * from github_repository order by name"
            }
            SortingOrder.Default -> {
                "SELECT * from github_repository"
            }
            else -> {
                "SELECT * from github_repository"
            }
        }
    }

    override fun getGithubRepository(id: Int): Observable<GithubRepository> {
        return Observable.fromCallable {
            return@fromCallable dataManager.getDataBaseHelper().githubRepositoryDao().findById(id)
        }
    }

    override fun saveGithubRepository(githubRepository: GithubRepository): Observable<Boolean> {
        return Observable.fromCallable {
            dataManager.getDataBaseHelper().githubRepositoryDao().insert(githubRepository)
            return@fromCallable true
        }
    }

    override fun saveGithubRepositoryList(githubRepositoryList: List<GithubRepository>): Observable<Boolean> {
        return Observable.fromCallable {
            dataManager.getDataBaseHelper().githubRepositoryDao().insertAll(githubRepositoryList)
            return@fromCallable true
        }
    }
}