package com.geetgobindingh.githubrepoapp.data.network

import com.geetgobindingh.githubrepoapp.data.network.service.GithubRepositoryService
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton


class NetworkHelper @Inject constructor(retrofit: Retrofit) {

    private var githubRepositoryService = retrofit.create(GithubRepositoryService::class.java)

    fun getGithubRepositoryService(): GithubRepositoryService {
        return githubRepositoryService
    }
}
