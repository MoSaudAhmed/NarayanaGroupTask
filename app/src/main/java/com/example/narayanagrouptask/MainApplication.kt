package com.example.narayanagrouptask

import com.example.narayanagrouptask.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class MainApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication>? {
        val injector = DaggerAppComponent.builder().create(this).build()
        return injector
    }
}