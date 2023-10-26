package com.geetgobindingh.githubrepoapp

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import com.geetgobindingh.githubrepoapp.utils.log.ReleaseTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    //region Member variables
    //endregion

    //region Application Callback methods
    override fun onCreate() {
        super.onCreate()
        setUp()
    }

    private fun setUp() {
        applicationInstance = this

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .permitDiskReads()
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .build()
            )
        }
    }
    //endregion

    //region Static Helper methods
    companion object {
        @get:Synchronized
        lateinit var applicationInstance: App
    }
    //endregion
}