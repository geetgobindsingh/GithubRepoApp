package com.geetgobindingh.githubrepoapp.ui.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModel
import com.geetgobindingh.githubrepoapp.util.api.NetworkUtil

abstract class BaseActivity<VB : ViewDataBinding, VM : ViewModel> : AppCompatActivity(), IView {

    private lateinit var mLifecycleRegistry: LifecycleRegistry
    private var mToast: Toast? = null
    private lateinit var mViewDataBinding: VB
    protected abstract val viewModel: VM
    //endregion

    //region Public API methods
    var activityContext: Context? = null
        private set

    override val isNetworkConnected: Boolean
        get() = NetworkUtil.isConnected

    override fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    //endregion

    //region Activity Lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLifecycleOwner()
        this.activityContext = this
        performDataBinding()
    }

    private fun performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        mViewDataBinding.setVariable(bindingVariable, viewModel)
        mViewDataBinding.lifecycleOwner = this
        addOtherVariables(mViewDataBinding)
        mViewDataBinding.executePendingBindings()
    }

    open fun addOtherVariables(mViewDataBinding: ViewDataBinding) {}

    open fun getViewDataBinding(): VB {
        return mViewDataBinding
    }
    //endregion

    //region Private Helper methods\
    private fun registerLifecycleOwner() {
        mLifecycleRegistry = LifecycleRegistry(this)
        mLifecycleRegistry.markState(Lifecycle.State.CREATED)
    }

    @SuppressLint("ShowToast")
    fun showToast(@StringRes message: Int) {
        if (mToast == null) {
            mToast = Toast.makeText(this, message, LENGTH_SHORT)
        } else {
            mToast?.setText(message)
        }
        mToast?.show()
    }

    @SuppressLint("ShowToast")
    fun showToast(message: String) {
        if (mToast == null) {
            mToast = Toast.makeText(this, message, LENGTH_SHORT)
        } else {
            mToast?.setText(message)
        }
        mToast?.show()
    }

    @SuppressLint("ShowToast")
    fun showToastLongDuration(message: String) {
        if (mToast == null) {
            mToast = Toast.makeText(this, message, LENGTH_LONG)
        } else {
            mToast?.setText(message)
        }
        mToast?.show()
    }
    //endregion

    //region IView implementation methods
    //endregion

    //region Private Helper Methods

    companion object {

    }
    //endregion

}
