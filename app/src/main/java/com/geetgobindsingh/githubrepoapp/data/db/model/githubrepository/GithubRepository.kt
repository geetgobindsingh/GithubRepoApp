package com.geetgobindingh.githubrepoapp.data.db.model.githubrepository

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "github_repository")
data class GithubRepository(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var author: String? = null,
    var avatar: String? = null,
    var currentPeriodStars: Int = 0,
    var description: String? = null,
    var forks: Int = 0,
    var language: String? = null,
    var languageColor: String? = null,
    var name: String? = null,
    var stars: Int = 0,
    var url: String? = null
)