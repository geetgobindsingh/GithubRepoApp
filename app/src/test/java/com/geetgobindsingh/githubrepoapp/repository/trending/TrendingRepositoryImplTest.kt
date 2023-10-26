package com.geetgobindingh.githubrepoapp.repository.trending

import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.test.core.app.ApplicationProvider
import com.geetgobindingh.githubrepoapp.data.DataManager
import com.geetgobindingh.githubrepoapp.data.DataManagerImpl
import com.geetgobindingh.githubrepoapp.data.db.model.githubrepository.GithubRepository
import com.geetgobindingh.githubrepoapp.data.db.model.githubrepository.GithubRepositoryDao
import com.geetgobindingh.githubrepoapp.data.mapper.githubrepository.GithubRepositoryMapper
import com.geetgobindingh.githubrepoapp.data.network.NetworkHelper
import com.geetgobindingh.githubrepoapp.data.network.model.githubrepository.ApiGithubRepositories
import com.geetgobindingh.githubrepoapp.data.prefs.SharedPrefHelper
import com.geetgobindingh.githubrepoapp.utils.file.FileReader
import com.gobasco.gobascoapp.data.db.AppDatabase
import com.gobasco.gobascoapp.data.db.DataBaseHelper
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.spy
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class TrendingRepositoryImplTest {

    private lateinit var context: Context
    private lateinit var trendingRepositoryImpl: TrendingRepositoryImpl
    private lateinit var db: AppDatabase
    private lateinit var dataManager: DataManager
    private lateinit var githubRepositoryDao: GithubRepositoryDao
    private var gson = Gson()
    private val githubRepositoryMapper = GithubRepositoryMapper()
    private var mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start()
        context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
        val sharedPrefHelper = SharedPrefHelper(
            context.getSharedPreferences(
                "gojek_sample_pref",
                Context.MODE_PRIVATE
            )
        )
        val dataBaseHelper = DataBaseHelper(db)
        val networkHelper = NetworkHelper(
            Retrofit.Builder()
                .baseUrl(mockWebServer.url(""))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        )
        dataManager = spy(
            DataManagerImpl(
                sharedPrefHelper,
                dataBaseHelper,
                networkHelper
            )
        )
        githubRepositoryDao = spy(dataManager.getDataBaseHelper().githubRepositoryDao())
        trendingRepositoryImpl = TrendingRepositoryImpl(
            dataManager, githubRepositoryMapper
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        db.close()
    }

    @Test
    fun getTrendingRepositoriesForceFetchRemoteTrueAndDefaultOrdering() {
        //Arrange
        val responseString =
            FileReader.getStringFromFile(context, "repositories_success_response.json")
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(responseString)
        val repoList =
            gson.fromJson<ApiGithubRepositories>(responseString, ApiGithubRepositories::class.java)
                .items
                .map {
                    GithubRepositoryMapper().map(it)
                }.toList()
        val params = TrendingRepositoryImpl.RepositoriesQueryParams()
        params.forceFetchFromRemote = true
        stubDataManagerIsCached(true)
        stubDataManagerIsExpired(false)
        stubDataManagerDeleteAllTablesData()
        mockWebServer.enqueue(response)
        //Act
        val testObserver = trendingRepositoryImpl.getTrendingRepositories(params).test()

        //Assert
        //verify(githubRepositoryDao, times(1)).getAllRepositories(SimpleSQLiteQuery("SELECT * from github_repository"))
        testObserver.assertValue(repoList)
        testObserver.dispose()
    }

    @Test
    fun getTrendingRepositoriesForceFetchRemoteTrueAndDefaultOrderingAndCacheExpired() {
        //Arrange
        val responseString =
            FileReader.getStringFromFile(context, "repositories_success_response.json")
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(responseString)
        val repoList =
            gson.fromJson<ApiGithubRepositories>(responseString, ApiGithubRepositories::class.java)
                .items
                .map {
                    GithubRepositoryMapper().map(it)
                }.toList()
        val params = TrendingRepositoryImpl.RepositoriesQueryParams()
        params.forceFetchFromRemote = true
        stubDataManagerIsCached(true)
        stubDataManagerIsExpired(true)
        stubDataManagerDeleteAllTablesData()
        mockWebServer.enqueue(response)
        //Act
        val testObserver = trendingRepositoryImpl.getTrendingRepositories(params).test()

        //Assert
        testObserver.assertValue(repoList)
        testObserver.dispose()
    }

    @Test
    fun getTrendingRepositoriesForceFetchRemoteTrueAndDefaultOrderingAndNoCachedResult() {
        //Arrange
        val responseString =
            FileReader.getStringFromFile(context, "repositories_success_response.json")
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(responseString)
        val repoList =
            gson.fromJson<ApiGithubRepositories>(responseString, ApiGithubRepositories::class.java)
                .items
                .map {
                    GithubRepositoryMapper().map(it)
                }.toList()
        val params = TrendingRepositoryImpl.RepositoriesQueryParams()
        params.forceFetchFromRemote = true
        stubDataManagerIsCached(false)
        stubDataManagerIsExpired(false)
        stubDataManagerDeleteAllTablesData()
        mockWebServer.enqueue(response)
        //Act
        val testObserver = trendingRepositoryImpl.getTrendingRepositories(params).test()

        //Assert
        testObserver.assertValue(repoList)
        testObserver.dispose()
    }

    @Test
    fun getTrendingRepositoriesForceFetchRemoteFalseAndDefaultOrdering() {
        //Arrange
        val repo1 = GithubRepository(
            id = 1,
            author = "Geet",
            name = "Trending Repository",
            avatar = "https://www.freepnglogos.com/uploads/instagram-logos-png-images-free-download-2.png",
            forks = 1222,
            stars = 9990,
            language = "Java",
            description = "Description"
        )
        val repo2 = GithubRepository(
            id = 2,
            author = "Geet",
            name = "Trending Repository",
            avatar = "https://www.freepnglogos.com/uploads/instagram-logos-png-images-free-download-2.png",
            forks = 1222,
            stars = 9990,
            language = "Java",
            description = "Description"
        )
        val repoList = listOf(repo1, repo2)
        val params = TrendingRepositoryImpl.RepositoriesQueryParams()
        params.forceFetchFromRemote = false
        stubDataManagerDeleteAllTablesData()
        stubDataManagerIsCached(true)
        stubDataManagerIsExpired(false)
        //Act
        trendingRepositoryImpl.saveGithubRepositoryList(repoList).test()
        //Assert
        val testObserver = trendingRepositoryImpl.getTrendingRepositories(params).test()
        testObserver.assertValue(repoList)
        testObserver.dispose()
    }

    @Test
    fun getTrendingRepositoriesForceFetchRemoteFalseAndSortByStars() {
        //Arrange
        val repo1 = GithubRepository(
            id = 1,
            author = "Geet",
            name = "Trending Repository",
            avatar = "https://www.freepnglogos.com/uploads/instagram-logos-png-images-free-download-2.png",
            forks = 1222,
            stars = 9990,
            language = "Java",
            description = "Description"
        )
        val repo2 = GithubRepository(
            id = 2,
            author = "Geet",
            name = "Trending Repository",
            avatar = "https://www.freepnglogos.com/uploads/instagram-logos-png-images-free-download-2.png",
            forks = 1223,
            stars = 9999,
            language = "Java",
            description = "Description"
        )
        val repoList = listOf(repo1, repo2)
        val params = TrendingRepositoryImpl.RepositoriesQueryParams()
        params.forceFetchFromRemote = false
        params.sortBy = TrendingRepositoryImpl.SortingOrder.ByStars
        stubDataManagerDeleteAllTablesData()
        stubDataManagerIsCached(true)
        stubDataManagerIsExpired(false)
        //Act
        trendingRepositoryImpl.saveGithubRepositoryList(repoList).test()
        //Assert
        val testObserver = trendingRepositoryImpl.getTrendingRepositories(params).test()
        testObserver.assertValue(repoList)
        testObserver.dispose()
    }

    @Test
    fun getTrendingRepositoriesForceFetchRemoteFalseAndSortByName() {
        //Arrange
        val repo1 = GithubRepository(
            id = 1,
            author = "Geet",
            name = "A - Trending Repository",
            avatar = "https://www.freepnglogos.com/uploads/instagram-logos-png-images-free-download-2.png",
            forks = 1223,
            stars = 9990,
            language = "Java",
            description = "Description"
        )
        val repo2 = GithubRepository(
            id = 2,
            author = "Geet",
            name = "B - Trending Repository",
            avatar = "https://www.freepnglogos.com/uploads/instagram-logos-png-images-free-download-2.png",
            forks = 1222,
            stars = 9999,
            language = "Java",
            description = "Description"
        )
        val repoList = listOf(repo1, repo2)
        val params = TrendingRepositoryImpl.RepositoriesQueryParams()
        params.forceFetchFromRemote = false
        params.sortBy = TrendingRepositoryImpl.SortingOrder.ByNames
        stubDataManagerDeleteAllTablesData()
        stubDataManagerIsCached(true)
        stubDataManagerIsExpired(false)
        //Act
        trendingRepositoryImpl.saveGithubRepositoryList(repoList).test()
        //Assert
        val testObserver = trendingRepositoryImpl.getTrendingRepositories(params).test()
        testObserver.assertValue(repoList)
        testObserver.dispose()
    }

    @Test
    fun saveGithubRepository() {
        //Arrange
        val repo = GithubRepository(
            id = 1,
            author = "Geet",
            name = "Trending Repository",
            avatar = "https://www.freepnglogos.com/uploads/instagram-logos-png-images-free-download-2.png",
            forks = 1222,
            stars = 9990,
            language = "Java",
            description = "Description"
        )
        //Act
        val testObserver1 = trendingRepositoryImpl.saveGithubRepository(repo).test()
        val testObserver2 = trendingRepositoryImpl.getGithubRepository(repo.id).test()
        //Assert
        testObserver1.assertValue(true)
        testObserver2.assertValue(repo)
        testObserver1.dispose()
        testObserver2.dispose()
    }

    @Test
    fun saveGithubRepositoryList() {
        //Arrange
        val repo1 = GithubRepository(
            id = 1,
            author = "Geet",
            name = "Trending Repository",
            avatar = "https://www.freepnglogos.com/uploads/instagram-logos-png-images-free-download-2.png",
            forks = 1222,
            stars = 9990,
            language = "Java",
            description = "Description"
        )
        val repo2 = GithubRepository(
            id = 2,
            author = "Geet",
            name = "Trending Repository",
            avatar = "https://www.freepnglogos.com/uploads/instagram-logos-png-images-free-download-2.png",
            forks = 1222,
            stars = 9990,
            language = "Java",
            description = "Description"
        )
        //Act
        val testObserver1 =
            trendingRepositoryImpl.saveGithubRepositoryList(listOf(repo1, repo2)).test()
        val testObserver2 = trendingRepositoryImpl.getGithubRepository(repo1.id).test()
        val testObserver3 = trendingRepositoryImpl.getGithubRepository(repo2.id).test()
        //Assert
        testObserver1.assertValue(true)
        testObserver2.assertValue(repo1)
        testObserver3.assertValue(repo2)
        testObserver1.dispose()
        testObserver2.dispose()
        testObserver3.dispose()
    }

    //region Private helper methods
    private fun stubDataManagerIsCached(isCached: Boolean) {
        doReturn(isCached).`when`(dataManager).isCached()
    }

    private fun stubDataManagerIsExpired(isExpired: Boolean) {
        doReturn(isExpired).`when`(dataManager).isExpired()
    }

    private fun stubDataManagerDeleteAllTablesData() {
        doReturn(true).`when`(dataManager).clearDb()
    }
    //endregion
}