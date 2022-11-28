package com.example.narayanagrouptask.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.narayanagrouptask.models.RepoItem

@Dao
interface NGTDao {

    @Query("SELECT * FROM RepoItem")
    fun getRepos(): List<RepoItem>?

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = RepoItem::class)
    fun insertReposInDB(reposList: List<RepoItem>?): LongArray?

    @Query("SELECT Count(*) FROM RepoItem")
    fun getReposCount(): Int
}