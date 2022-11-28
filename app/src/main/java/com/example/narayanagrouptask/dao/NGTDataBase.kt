package com.example.narayanagrouptask.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.narayanagrouptask.models.*

@Database(
    entities =
    [RepoItem::class], version = 1, exportSchema = false
)

@TypeConverters(
    HomeRepoListConverters::class,
    OwnerListConverters::class,
    LicenseListConverters::class,
    StringConverters::class
)


abstract class NGTDataBase : RoomDatabase() {
    abstract fun ngtDataBase(): NGTDao?
}