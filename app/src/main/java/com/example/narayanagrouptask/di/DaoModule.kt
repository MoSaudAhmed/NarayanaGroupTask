package com.example.narayanagrouptask.di

import android.app.Application
import androidx.room.Room
import com.example.narayanagrouptask.dao.NGTDao
import com.example.narayanagrouptask.dao.NGTDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DaoModule {

    @Provides
    @Singleton
    fun provideNGTDao(app: Application): NGTDao? {
        val db: NGTDataBase = Room.databaseBuilder(
            app,
            NGTDataBase::class.java, "narayanaGroupTask-db"
        ).build()
        return db.ngtDataBase()
    }
}