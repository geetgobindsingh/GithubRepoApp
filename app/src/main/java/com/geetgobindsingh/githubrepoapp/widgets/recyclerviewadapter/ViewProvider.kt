package com.geetgobindingh.githubrepoapp.widgets.recyclerviewadapter

import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil

interface ViewProvider {
    @LayoutRes
    fun getLayoutId(model: Class<out ViewModel>): Int

    fun getLifeCycleOwner(): LifecycleOwner? {
        return null
    }

    fun getDiffUtilItemCallback(): DiffUtil.ItemCallback<ListViewModel> {
        return object : DiffUtil.ItemCallback<ListViewModel>() {
            override fun areItemsTheSame(oldItem: ListViewModel, newItem: ListViewModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListViewModel, newItem: ListViewModel): Boolean {
                return false
            }

        }
    }
}