package com.geetgobindingh.githubrepoapp.ui.base

interface IViewModel<V : IView> {

    val view: V?

    val isViewAttached: Boolean
}