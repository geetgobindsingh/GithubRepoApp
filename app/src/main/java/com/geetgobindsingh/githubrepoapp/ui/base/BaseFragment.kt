package com.geetgobindingh.githubrepoapp.ui.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

abstract class BaseFragment<VB : ViewDataBinding, VM : ViewModel> : AppCompatDialogFragment(),
    IView {

    //region Member variables
    private var mToast: Toast? = null
    private lateinit var mViewDataBinding: VB
    protected abstract val viewModel: VM
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return mViewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.setVariable(bindingVariable, viewModel)
        mViewDataBinding.lifecycleOwner = this
        mViewDataBinding.executePendingBindings()
    }

    open fun getViewDataBinding(): VB {
        return mViewDataBinding
    }

    @SuppressLint("ShowToast")
    fun showToast(@StringRes message: Int) {
        if (mToast == null) {
            mToast = Toast.makeText(context, message, LENGTH_SHORT)
        } else {
            mToast?.setText(message)
        }
        mToast?.show()
    }

    @SuppressLint("ShowToast")
    fun showToast(message: String) {
        if (mToast == null) {
            mToast = Toast.makeText(context, message, LENGTH_SHORT)
        } else {
            mToast?.setText(message)
        }
        mToast?.show()
    }

    @SuppressLint("ShowToast")
    fun showToastLongDuration(message: String) {
        if (mToast == null) {
            mToast = Toast.makeText(context, message, LENGTH_LONG)
        } else {
            mToast?.setText(message)
        }
        mToast?.show()
    }
}
