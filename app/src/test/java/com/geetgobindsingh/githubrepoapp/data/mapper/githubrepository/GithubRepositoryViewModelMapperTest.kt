package com.geetgobindingh.githubrepoapp.data.mapper.githubrepository

import com.geetgobindingh.githubrepoapp.data.db.model.githubrepository.GithubRepository
import com.geetgobindingh.githubrepoapp.ui.common.GithubRepositoryViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GithubRepositoryViewModelMapperTest {

    private lateinit var githubRepositoryViewModelMapper: GithubRepositoryViewModelMapper

    @Before
    fun setUp() {
        githubRepositoryViewModelMapper = GithubRepositoryViewModelMapper()
    }

    @Test
    fun map() {
        //Arrange
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
        val viewModel = GithubRepositoryViewModel(repo)
        //Act
        val obj = githubRepositoryViewModelMapper.map(repo)
        //Assert
        Assert.assertEquals(viewModel, obj)
    }
}