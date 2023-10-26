package com.geetgobindingh.githubrepoapp.data.network.model.githubrepository

data class ApiGithubRepositories(
    val incomplete_results: Boolean,
    val items: List<ApiGithubRepository>,
    val total_count: Int
)