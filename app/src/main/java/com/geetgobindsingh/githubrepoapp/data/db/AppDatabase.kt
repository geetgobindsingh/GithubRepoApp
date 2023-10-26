package com.gobasco.gobascoapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.geetgobindingh.githubrepoapp.data.db.DaoFactory
import com.geetgobindingh.githubrepoapp.data.db.model.githubrepository.GithubRepository

@Database(
    entities =
    [
        GithubRepository::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase(), DaoFactory
