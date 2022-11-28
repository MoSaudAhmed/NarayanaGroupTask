package com.example.narayanagrouptask.di

import com.example.narayanagrouptask.ui.activities.HomeActivity
import com.example.narayanagrouptask.ui.activities.DetailsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    abstract fun homeActivity(): HomeActivity

    @ContributesAndroidInjector
    abstract fun detailActivity(): DetailsActivity

}
