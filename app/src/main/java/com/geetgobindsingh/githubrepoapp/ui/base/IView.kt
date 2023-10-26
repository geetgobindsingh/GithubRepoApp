package com.geetgobindingh.githubrepoapp.ui.base

interface IView {

    val isNetworkConnected: Boolean

    fun hideKeyboard()

    val bindingVariable: Int

    val layoutId: Int
}

