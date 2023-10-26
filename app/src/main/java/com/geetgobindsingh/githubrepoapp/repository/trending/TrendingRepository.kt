package com.geetgobindingh.githubrepoapp.repository.trending

import com.geetgobindingh.githubrepoapp.data.db.model.githubrepository.GithubRepository
import com.geetgobindingh.githubrepoapp.repository.base.Repository
import io.reactivex.Observable

interface TrendingRepository : Repository {
    fun saveGithubRepository(githubRepository: GithubRepository): Observable<Boolean>
    fun saveGithubRepositoryList(githubRepositoryList: List<GithubRepository>): Observable<Boolean>
    fun getGithubRepository(id: Int): Observable<GithubRepository>
    fun getTrendingRepositories(queryParams: TrendingRepositoryImpl.RepositoriesQueryParams): Observable<List<GithubRepository>>
}