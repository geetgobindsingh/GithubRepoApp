package com.geetgobindingh.githubrepoapp.ui.trending.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.geetgobindingh.githubrepoapp.data.db.model.githubrepository.GithubRepository
import com.geetgobindingh.githubrepoapp.data.mapper.githubrepository.GithubRepositoryViewModelMapper
import com.geetgobindingh.githubrepoapp.repository.trending.TrendingRepository
import com.geetgobindingh.githubrepoapp.repository.trending.TrendingRepositoryImpl
import com.geetgobindingh.githubrepoapp.ui.base.BaseViewModel
import com.geetgobindingh.githubrepoapp.ui.base.IView
import com.geetgobindingh.githubrepoapp.ui.common.GithubRepositoryViewModel
import com.geetgobindingh.githubrepoapp.ui.trending.view.ITrendingView
import com.geetgobindingh.githubrepoapp.util.rx.scheduler.SchedulerProvider
import com.geetgobindingh.githubrepoapp.utils.log.Logger
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
public class TrendingViewModel<V : ITrendingView>  @Inject constructor(
    iView: IView,
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    private val trendingRepository: TrendingRepository,
) : BaseViewModel<V>(iView, schedulerProvider, compositeDisposable) {

    //region Member Variables
    private val trendingRepositories = MutableLiveData<List<GithubRepositoryViewModel>>()
    private val showLoadingProgressBar = MutableLiveData<Boolean>(true)
    private val showSwipeRefreshView = MutableLiveData<Boolean>(true)
    private val showListView = MutableLiveData<Boolean>(true)
    private val showSwipeRefreshProgress = MutableLiveData<Boolean>(false)
    private val showErrorScreen = MutableLiveData<Boolean>(false)
    private val showEmptyScreen = MutableLiveData<Boolean>(false)

    private var mSelectedItemIndex = -1
    private var queryParams = TrendingRepositoryImpl.RepositoriesQueryParams()
    private val githubRepositoryViewModelMapper = GithubRepositoryViewModelMapper()
    //endregion

    //region Constructor methods
    init {
        sortListByDefault()
    }
    //endregion

    //region Public API methods
    fun getTrendingRepositories(): LiveData<List<GithubRepositoryViewModel>> {
        return trendingRepositories
    }

    fun sortListByStars() {
        queryParams.sortBy = TrendingRepositoryImpl.SortingOrder.ByStars
        fetchTrendingRepositories()
    }

    fun sortListByDefault() {
        queryParams.sortBy = TrendingRepositoryImpl.SortingOrder.Default
        fetchTrendingRepositories()
    }

    fun sortListByNames() {
        queryParams.sortBy = TrendingRepositoryImpl.SortingOrder.ByNames
        fetchTrendingRepositories()
    }

    fun forceFetchFromRemote() {
        queryParams.forceFetchFromRemote = true
        fetchTrendingRepositories()
    }

    fun showLoadingProgressBar(): LiveData<Boolean> {
        return showLoadingProgressBar
    }

    fun showSwipeRefreshView(): LiveData<Boolean> {
        return showSwipeRefreshView
    }

    fun showListView(): LiveData<Boolean> {
        return showListView
    }

    fun showSwipeRefreshProgress(): LiveData<Boolean> {
        return showSwipeRefreshProgress
    }

    fun showErrorScreen(): LiveData<Boolean> {
        return showErrorScreen
    }

    fun showEmptyScreen(): LiveData<Boolean> {
        return showEmptyScreen
    }

    fun setQueryParams(queryParams: TrendingRepositoryImpl.RepositoriesQueryParams) {
        this.queryParams = queryParams
    }

    fun getQueryParams(): TrendingRepositoryImpl.RepositoriesQueryParams {
        return this.queryParams
    }
    //endregion

    //region Private Helper methods
    private fun fetchTrendingRepositories() {
        onLoadState(getQueryParams().forceFetchFromRemote)
        mCompositeDisposable.add(Observable
            .fromCallable {
                trendingRepository.getTrendingRepositories(getQueryParams())
            }
            .flatMap(this::githubRepositoryViewModelList)
            .map {
                getQueryParams().forceFetchFromRemote = false
                it
            }
            .subscribeOn(mSchedulerProvider.io())
            .observeOn(mSchedulerProvider.ui())
            .subscribe({ trendingRepositoryList ->
                trendingRepositories.value = trendingRepositoryList
                onSuccessState(trendingRepositoryList)
            }, { t ->
                onErrorState()
                Logger.e(t)
            })
        )
    }

    private fun onLoadState(forceFetchFromRemote: Boolean) {
        showSwipeRefreshView.postValue(forceFetchFromRemote)
        showSwipeRefreshProgress.postValue(forceFetchFromRemote)
        showLoadingProgressBar.postValue(!forceFetchFromRemote)
        showErrorScreen.postValue(false)
        showEmptyScreen.postValue(false)
    }

    private fun onSuccessState(trendingRepositoryList: List<GithubRepositoryViewModel>) {
        showSwipeRefreshView.postValue(true)
        showListView.postValue(true)
        showLoadingProgressBar.postValue(false)
        showSwipeRefreshProgress.postValue(false)
        showEmptyScreen.postValue(trendingRepositoryList.isEmpty())
        showErrorScreen.postValue(false)
    }

    private fun onErrorState() {
        showSwipeRefreshView.postValue(true)
        showListView.postValue(false)
        showLoadingProgressBar.postValue(false)
        showSwipeRefreshProgress.postValue(false)
        showEmptyScreen.postValue(false)
        showErrorScreen.postValue(true)
    }

    private fun githubRepositoryViewModelList(usersList: Observable<List<GithubRepository>>): Observable<List<GithubRepositoryViewModel>> {
        return usersList.flatMap {
            Observable.fromIterable(it)
                .map { repo ->
                    githubRepositoryViewModelMapper.map(repo)
                }
                .map { viewModel ->
                    viewModel.setListener(object :
                        GithubRepositoryViewModel.GithubRepositoryViewModelListener {
                        override fun onItemClick(githubRepository: GithubRepository) {
                            resetSelectedItemState(githubRepository)
                        }
                    })
                    return@map viewModel
                }.toList().toObservable()
        }
    }

    private fun resetSelectedItemState(githubRepository: GithubRepository) {
        val list = trendingRepositories.value?.toMutableList()
        val index = list?.indexOfFirst {
            it.githubRepository.id == githubRepository.id
        }
        index?.let { currentIndex ->
            if (mSelectedItemIndex == -1) {
                list[currentIndex].selected.postValue(true)
            } else if (mSelectedItemIndex != index) {
                list[currentIndex].selected.postValue(true)
                list[mSelectedItemIndex].selected.postValue(false)
            }
            mSelectedItemIndex = currentIndex
        }
        trendingRepositories.postValue(list)
    }
    //endregion
}