package com.gobasco.gobascoapp.data.db

import android.os.Looper
import com.geetgobindingh.githubrepoapp.data.db.DaoFactory
import com.geetgobindingh.githubrepoapp.data.db.model.githubrepository.GithubRepositoryDao
import javax.inject.Inject
import javax.inject.Singleton


class DataBaseHelper @Inject
constructor(private val appDatabase: AppDatabase) : DaoFactory {

    @Throws(Exception::class)
    fun deleteAllTablesData(): Boolean {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw Exception("Do Not Run this in Main Thread!")
        } else {
            appDatabase.clearAllTables()
            return true
        }
    }

    override fun githubRepositoryDao(): GithubRepositoryDao {
        return appDatabase.githubRepositoryDao()
    }
}
