package com.geetgobindingh.githubrepoapp.di.module

import com.geetgobindingh.githubrepoapp.util.rx.scheduler.AppSchedulerProvider
import com.geetgobindingh.githubrepoapp.util.rx.scheduler.SchedulerProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.disposables.CompositeDisposable

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    @ViewModelScoped
    fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @Provides
    @ViewModelScoped
    fun provideSchedulerProvider(): SchedulerProvider {
        return AppSchedulerProvider()
    }
}