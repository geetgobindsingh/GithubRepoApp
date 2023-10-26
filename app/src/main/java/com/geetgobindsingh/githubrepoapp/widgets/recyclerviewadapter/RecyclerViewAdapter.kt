package com.geetgobindingh.githubrepoapp.widgets.recyclerviewadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.geetgobindingh.githubrepoapp.BR

class RecyclerViewAdapter(
    private val viewProvider: ViewProvider
) : ListAdapter<ListViewModel, RecyclerViewAdapter.ViewHolder>(viewProvider.getDiffUtilItemCallback()) {

    override fun getItemViewType(position: Int): Int {
        return viewProvider.getLayoutId(getItem(position)::class.java)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            DataBindingUtil.inflate(inflater, viewType, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewDataBinding.setVariable(BR.viewModel, getItem(position))
        holder.viewDataBinding.lifecycleOwner = viewProvider.getLifeCycleOwner()
        holder.viewDataBinding.executePendingBindings()
    }

    fun updateData(newViewModels: List<ListViewModel>) {
        submitList(newViewModels)
    }

    class ViewHolder(val viewDataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root)
}