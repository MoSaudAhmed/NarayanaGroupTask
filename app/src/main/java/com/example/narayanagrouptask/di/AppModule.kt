package com.example.narayanagrouptask.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides

@Module(includes = [(ViewModelModule::class)])
class AppModule {

    @Provides
    fun provideSharedPreferences(app: Application): SharedPreferences {
        return app.getSharedPreferences("pref", Context.MODE_PRIVATE)
    }
}