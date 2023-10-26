package com.geetgobindsingh.githubrepoapp.di.module

import android.app.Activity
import com.geetgobindingh.githubrepoapp.ui.base.BaseActivity
import com.geetgobindingh.githubrepoapp.ui.base.IView
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    @ActivityScoped
    fun provideIView(activity: Activity?): IView {
        return activity as BaseActivity<*, *>
    }

}