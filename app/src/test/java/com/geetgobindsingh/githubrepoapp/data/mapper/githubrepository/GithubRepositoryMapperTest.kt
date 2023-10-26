package com.geetgobindingh.githubrepoapp.data.mapper.githubrepository

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.geetgobindingh.githubrepoapp.data.db.model.githubrepository.GithubRepository
import com.geetgobindingh.githubrepoapp.data.network.model.githubrepository.ApiGithubRepositories
import com.geetgobindingh.githubrepoapp.utils.file.FileReader
import com.google.gson.Gson
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class GithubRepositoryMapperTest {

    private lateinit var context: Context
    private lateinit var githubRepositoryMapper: GithubRepositoryMapper
    private var gson = Gson()

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        githubRepositoryMapper = GithubRepositoryMapper()
    }

    @Test
    fun map() {
        //Arrange
        val responseString =
            FileReader.getStringFromFile(context, "repositories_success_response.json")
        val apiGithubRepository = gson.fromJson<ApiGithubRepositories>(
            responseString,
            ApiGithubRepositories::class.java
        ).items[0]
        val repo = GithubRepository(18408635).apply {
            author = "chrislgarry"
            avatar = "https://avatars1.githubusercontent.com/u/2200898?v=4"
            currentPeriodStars = 49587
            description =
                "Original Apollo 11 Guidance Computer (AGC) source code for the command and lunar modules."
            forks = 6604
            language = "Assembly"
            languageColor = null
            name = "Apollo-11"
            stars = 49587
            url = "https://api.github.com/repos/chrislgarry/Apollo-11"
        }

        //Act
        val obj =  githubRepositoryMapper.map(apiGithubRepository)
        //Assert
        Assert.assertEquals(repo, obj)
    }
}