package com.geetgobindingh.githubrepoapp.data.mapper.githubrepository

import com.geetgobindingh.githubrepoapp.data.db.model.githubrepository.GithubRepository
import com.geetgobindingh.githubrepoapp.data.mapper.base.Mapper
import com.geetgobindingh.githubrepoapp.data.network.model.githubrepository.ApiGithubRepository
import javax.inject.Inject

class GithubRepositoryMapper :
    Mapper<ApiGithubRepository, GithubRepository> {

    override fun map(it: ApiGithubRepository): GithubRepository {
        return GithubRepository(it.id).apply {
            author = it.owner.login
            avatar = it.owner.avatar_url
            currentPeriodStars = it.stargazers_count
            description = it.description
            forks = it.forks
            language = it.language
            languageColor = null
            name = it.name
            stars = it.stargazers_count
            url = it.url
        }
    }

}