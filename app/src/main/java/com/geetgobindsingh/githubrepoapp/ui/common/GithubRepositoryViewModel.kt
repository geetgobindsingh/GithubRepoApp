package com.geetgobindingh.githubrepoapp.ui.common

import androidx.lifecycle.MutableLiveData
import com.geetgobindingh.githubrepoapp.data.db.model.githubrepository.GithubRepository
import com.geetgobindingh.githubrepoapp.widgets.recyclerviewadapter.ListViewModel

class GithubRepositoryViewModel(
    val githubRepository: GithubRepository,
    private var mListener: GithubRepositoryViewModelListener? = null
) : ListViewModel() {

    //region Member variables
    //endregion

    //region View Variables
    var selected = MutableLiveData<Boolean>(false)
    //endregion

    //region constructor methods
    init {
    }
    //endregion

    //Public API methods
    fun setListener(listener: GithubRepositoryViewModelListener) {
        mListener = listener
    }

    fun onItemClick() {
        mListener?.onItemClick(githubRepository)
    }

    override fun hashCode(): Int {
        return githubRepository.hashCode()
    }
    //endregion

    interface GithubRepositoryViewModelListener {
        fun onItemClick(githubRepository: GithubRepository)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GithubRepositoryViewModel

        if (githubRepository != other.githubRepository) return false

        return true
    }
}