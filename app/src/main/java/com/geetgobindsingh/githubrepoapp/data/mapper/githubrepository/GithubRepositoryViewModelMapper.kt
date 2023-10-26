package com.geetgobindingh.githubrepoapp.data.mapper.githubrepository

import com.geetgobindingh.githubrepoapp.data.db.model.githubrepository.GithubRepository
import com.geetgobindingh.githubrepoapp.data.mapper.base.Mapper
import com.geetgobindingh.githubrepoapp.ui.common.GithubRepositoryViewModel

class GithubRepositoryViewModelMapper :
    Mapper<GithubRepository, GithubRepositoryViewModel> {

    override fun map(it: GithubRepository): GithubRepositoryViewModel {
        return GithubRepositoryViewModel(it)
    }
}