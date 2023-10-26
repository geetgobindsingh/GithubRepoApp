package com.geetgobindingh.githubrepoapp.util.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.geetgobindingh.githubrepoapp.App

object NetworkUtil {

    /**
     * Check if there is any connectivity
     */
    val isConnected: Boolean
        get() {
            val context = App.applicationInstance.applicationContext
            context?.let {
                val info = NetworkUtil.getNetworkInfo(context)
                return info != null && info.isConnected
            }
            return false
        }

    /**
     * Get the network info
     */
    fun getNetworkInfo(context: Context): NetworkInfo? {
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }
}
