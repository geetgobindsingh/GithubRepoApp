package com.geetgobindingh.githubrepoapp.data

import com.geetgobindingh.githubrepoapp.data.network.NetworkHelper
import com.geetgobindingh.githubrepoapp.data.prefs.SharedPrefHelper
import com.gobasco.gobascoapp.data.db.DataBaseHelper

interface DataManager {
    fun setLastCacheTime(lastCache: Long)
    fun isCached(): Boolean
    fun isExpired(): Boolean
    fun getLastCacheUpdateTimeMillis(): Long
    fun getSharedPrefHelper(): SharedPrefHelper
    fun getDataBaseHelper(): DataBaseHelper
    fun getNetworkBaseHelper(): NetworkHelper
    fun clearDb(): Boolean
}
