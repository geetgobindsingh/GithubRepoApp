package com.geetgobindingh.githubrepoapp.data.network.service

import com.geetgobindingh.githubrepoapp.data.network.model.githubrepository.ApiGithubRepositories
import io.reactivex.Observable
import retrofit2.http.GET

interface GithubRepositoryService {

    @GET("search/repositories?q=language:assembly&sort=stars&order=desc")
    fun fetchTrendingRepositories(): Observable<ApiGithubRepositories>
}