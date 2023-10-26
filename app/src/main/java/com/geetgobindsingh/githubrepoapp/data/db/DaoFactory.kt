package com.geetgobindingh.githubrepoapp.data.db

import com.geetgobindingh.githubrepoapp.data.db.model.githubrepository.GithubRepositoryDao

interface DaoFactory {
    fun githubRepositoryDao(): GithubRepositoryDao
}