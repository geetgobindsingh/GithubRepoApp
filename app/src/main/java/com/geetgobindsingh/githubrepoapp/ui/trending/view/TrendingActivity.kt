package com.geetgobindingh.githubrepoapp.ui.trending.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.geetgobindingh.githubrepoapp.BR
import com.geetgobindingh.githubrepoapp.R
import com.geetgobindingh.githubrepoapp.databinding.ActivityTrendingBinding
import com.geetgobindingh.githubrepoapp.ui.base.BaseActivity
import com.geetgobindingh.githubrepoapp.ui.common.GithubRepositoryViewModel
import com.geetgobindingh.githubrepoapp.ui.trending.viewmodel.TrendingViewModel
import com.geetgobindingh.githubrepoapp.widgets.recyclerviewadapter.ListViewModel
import com.geetgobindingh.githubrepoapp.widgets.recyclerviewadapter.ViewProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TrendingActivity : BaseActivity<ActivityTrendingBinding,
        TrendingViewModel<ITrendingView>>(), ITrendingView {
    //region Member variables
    override val viewModel: TrendingViewModel<ITrendingView> by viewModels()
    override val bindingVariable = BR.viewModel
    override val layoutId = R.layout.activity_trending
    private val activityTrendingBinding by lazy {
        return@lazy getViewDataBinding()
    }
    //endregion

    //region View Variables
    //endregion

    //region Activity Callback methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(activityTrendingBinding.layoutToolbar.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        with(activityTrendingBinding.trendingRepoRecyclerView) {
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                    ContextCompat.getDrawable(context, R.drawable.shape_background_divider)?.let {
                        setDrawable(it)
                    }
                }
            )
        }
        activityTrendingBinding.layoutToolbar.toolbarTitleTextView.text =
            getString(R.string.text_trending)
        activityTrendingBinding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.forceFetchFromRemote()
        }
    }

    override fun addOtherVariables(mViewDataBinding: ViewDataBinding) {
        super.addOtherVariables(mViewDataBinding)
        mViewDataBinding.setVariable(BR.viewProvider, object : ViewProvider {
            override fun getLayoutId(model: Class<out ViewModel>): Int {
                return when (model) {
                    GithubRepositoryViewModel::class.java -> {
                        R.layout.list_item_github_repository
                    }
                    else -> {
                        0
                    }
                }
            }

            override fun getLifeCycleOwner(): LifecycleOwner? {
                return this@TrendingActivity
            }

            override fun getDiffUtilItemCallback(): DiffUtil.ItemCallback<ListViewModel> {
                return object : DiffUtil.ItemCallback<ListViewModel>() {
                    override fun areItemsTheSame(
                        oldItem: ListViewModel,
                        newItem: ListViewModel
                    ): Boolean {
                        if (oldItem is GithubRepositoryViewModel && newItem is GithubRepositoryViewModel) {
                            if (oldItem.githubRepository.id == newItem.githubRepository.id) {
                                return true
                            }
                        }
                        return false
                    }

                    override fun areContentsTheSame(
                        oldItem: ListViewModel,
                        newItem: ListViewModel
                    ): Boolean {
                        if (oldItem is GithubRepositoryViewModel && newItem is GithubRepositoryViewModel) {
                            if (oldItem.equals(newItem)) {
                                return true
                            }
                        }
                        return false
                    }

                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_trending, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                finish()
                true
            }
            R.id.sort_by_stars -> {
                viewModel.sortListByStars()
                true
            }
            R.id.sort_by_name -> {
                viewModel.sortListByNames()
                true
            }
            R.id.sort_by_default -> {
                viewModel.sortListByDefault()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
    //endregion

    //region Private Helper methods
    //endregion
}