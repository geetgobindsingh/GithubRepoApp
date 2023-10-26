package com.geetgobindingh.githubrepoapp.data.db.model.githubrepository

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface GithubRepositoryDao {

    @RawQuery
    fun getAllRepositories(query: SupportSQLiteQuery): List<GithubRepository>

    @Query("SELECT * from github_repository")
    fun getAllRepositories(): List<GithubRepository>

    @Query("SELECT * from github_repository ORDER BY id LIMIT 1")
    fun containsRecords(): Int

    @Query("SELECT * FROM github_repository WHERE id = :id")
    fun findById(id: Int): GithubRepository?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(repository: GithubRepository)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(repositories: List<GithubRepository>)
}