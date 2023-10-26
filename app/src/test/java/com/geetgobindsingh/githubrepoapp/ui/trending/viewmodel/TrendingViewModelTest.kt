package com.geetgobindingh.githubrepoapp.ui.trending.viewmodel

import android.content.Context
import android.os.Build
import androidx.lifecycle.SavedStateHandle
import androidx.test.core.app.ApplicationProvider
import com.geetgobindingh.githubrepoapp.data.mapper.githubrepository.GithubRepositoryMapper
import com.geetgobindingh.githubrepoapp.data.mapper.githubrepository.GithubRepositoryViewModelMapper
import com.geetgobindingh.githubrepoapp.data.network.model.githubrepository.ApiGithubRepositories
import com.geetgobindingh.githubrepoapp.getOrAwaitValue
import com.geetgobindingh.githubrepoapp.repository.trending.TrendingRepository
import com.geetgobindingh.githubrepoapp.repository.trending.TrendingRepositoryImpl
import com.geetgobindingh.githubrepoapp.ui.common.GithubRepositoryViewModel
import com.geetgobindingh.githubrepoapp.ui.trending.view.ITrendingView
import com.geetgobindingh.githubrepoapp.utils.file.FileReader
import com.geetgobindingh.githubrepoapp.utils.rx.scheduler.TestSchedulerProvider
import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.spy
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class TrendingViewModelTest {

    private lateinit var context: Context
    private lateinit var trendingViewModel: TrendingViewModel<ITrendingView>
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var testScheduler: TestScheduler
    private var gson = Gson()
    private val params = TrendingRepositoryImpl.RepositoriesQueryParams()
    private val githubRepositoryMapper = GithubRepositoryMapper()
    private val githubRepositoryViewModelMapper = GithubRepositoryViewModelMapper()

    @Mock
    lateinit var iTrendingView: ITrendingView

    @Mock
    lateinit var trendingRepository: TrendingRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        context = ApplicationProvider.getApplicationContext()
        compositeDisposable = CompositeDisposable()
        testScheduler = TestScheduler()
        trendingViewModel = spy(
            TrendingViewModel(
                iTrendingView,
                TestSchedulerProvider(testScheduler),
                compositeDisposable,
                trendingRepository,
                SavedStateHandle()
            )
        )
    }

    @After
    fun tearDown() {
        compositeDisposable.dispose()
    }

    @Test
    fun fetchTrendingRepositoriesSuccess() {
        //Arrange
        val responseString =
            FileReader.getStringFromFile(context, "repositories_success_response.json")
        val repoViewModelList =
            gson.fromJson<ApiGithubRepositories>(responseString, ApiGithubRepositories::class.java)
                .items
                .map {
                    githubRepositoryMapper.map(it)
                }
                .toList()
        trendingViewModel.setQueryParams(params)
        Mockito.`when`(trendingRepository.getTrendingRepositories(trendingViewModel.getQueryParams()))
            .thenReturn(Observable.just(repoViewModelList))

        //Act
        trendingViewModel.forceFetchFromRemote()

        //Assert
        assertOnLoadState(true)
        testScheduler.triggerActions()
        assertOnSuccessState(repoViewModelList.map {
            githubRepositoryViewModelMapper.map(it)
        })
    }

    @Test
    fun fetchTrendingRepositoriesFailure() {
        //Arrange
        trendingViewModel.setQueryParams(params)
        Mockito.`when`(trendingRepository.getTrendingRepositories(trendingViewModel.getQueryParams()))
            .thenReturn(Observable.error(Exception("")))

        //Act
        trendingViewModel.forceFetchFromRemote()

        //Assert
        assertOnLoadState(true)
        testScheduler.triggerActions()
        assertErrorState()
    }

    private fun assertOnLoadState(forceFetchFromRemote: Boolean) {
        assertThat(
            trendingViewModel.showSwipeRefreshView().getOrAwaitValue(),
            `is`(forceFetchFromRemote)
        )
        assertThat(
            trendingViewModel.showSwipeRefreshProgress().getOrAwaitValue(),
            `is`(forceFetchFromRemote)
        )
        assertThat(
            trendingViewModel.showLoadingProgressBar().getOrAwaitValue(),
            `is`(!forceFetchFromRemote)
        )
        assertThat(trendingViewModel.showErrorScreen().getOrAwaitValue(), `is`(false))
        assertThat(trendingViewModel.showEmptyScreen().getOrAwaitValue(), `is`(false))
    }

    private fun assertOnSuccessState(trendingRepositoryList: List<GithubRepositoryViewModel>) {
        assertThat(trendingViewModel.showSwipeRefreshView().getOrAwaitValue(), `is`(true))
        assertThat(trendingViewModel.showListView().getOrAwaitValue(), `is`(true))
        assertThat(trendingViewModel.showLoadingProgressBar().getOrAwaitValue(), `is`(false))
        assertThat(trendingViewModel.showSwipeRefreshProgress().getOrAwaitValue(), `is`(false))
        assertThat(
            trendingViewModel.showEmptyScreen().getOrAwaitValue(),
            `is`(trendingRepositoryList.isEmpty())
        )
        assertThat(trendingViewModel.showErrorScreen().getOrAwaitValue(), `is`(false))
    }

    private fun assertErrorState() {
        assertThat(trendingViewModel.showSwipeRefreshView().getOrAwaitValue(), `is`(true))
        assertThat(trendingViewModel.showListView().getOrAwaitValue(), `is`(false))
        assertThat(trendingViewModel.showLoadingProgressBar().getOrAwaitValue(), `is`(false))
        assertThat(trendingViewModel.showSwipeRefreshProgress().getOrAwaitValue(), `is`(false))
        assertThat(
            trendingViewModel.showEmptyScreen().getOrAwaitValue(),
            `is`(false)
        )
        assertThat(trendingViewModel.showErrorScreen().getOrAwaitValue(), `is`(true))
    }

}