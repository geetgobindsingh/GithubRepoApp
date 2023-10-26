package com.geetgobindingh.githubrepoapp.data

import android.os.Looper
import com.geetgobindingh.githubrepoapp.data.network.NetworkHelper
import com.geetgobindingh.githubrepoapp.data.prefs.SharedPrefHelper
import com.geetgobindingh.githubrepoapp.data.prefs.SharedPrefKeys
import com.gobasco.gobascoapp.data.db.DataBaseHelper

class DataManagerImpl
constructor(
    private val sharedPrefHelper: SharedPrefHelper,
    private val dataBaseHelper: DataBaseHelper,
    private val networkHelper: NetworkHelper
) : DataManager {

    private val EXPIRATION_TIME = (45 * 60 * 1000).toLong() // 45 minutes

    override fun setLastCacheTime(lastCache: Long) {
        getSharedPrefHelper().saveData(SharedPrefKeys.LAST_CACHE_TIME, lastCache)
    }

    override fun isCached(): Boolean {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw Exception("Do Not Run this in Main Thread!")
        } else {
            return getDataBaseHelper().githubRepositoryDao().containsRecords() > 0
        }
    }

    override fun isExpired(): Boolean {
        val currentTime = System.currentTimeMillis()
        val lastUpdateTime = this.getLastCacheUpdateTimeMillis()
        return currentTime - lastUpdateTime > EXPIRATION_TIME
    }

    override fun getLastCacheUpdateTimeMillis(): Long {
        return getSharedPrefHelper().getLong(SharedPrefKeys.LAST_CACHE_TIME, 0L)
    }

    override fun getSharedPrefHelper(): SharedPrefHelper {
        return sharedPrefHelper
    }

    override fun getDataBaseHelper(): DataBaseHelper {
        return dataBaseHelper
    }

    override fun getNetworkBaseHelper(): NetworkHelper {
        return networkHelper
    }

    override fun clearDb(): Boolean {
        return dataBaseHelper.deleteAllTablesData()
    }

}
