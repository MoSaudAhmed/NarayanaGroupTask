package com.example.narayanagrouptask.di

import android.app.Application
import com.example.narayanagrouptask.MainApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [(AndroidSupportInjectionModule::class), (ActivityBindingModule::class), (FragmentBindingModule::class), (NetworkModule::class),
        (AppModule::class),(DaoModule::class)]
)
interface AppComponent : AndroidInjector<DaggerApplication> {

    fun inject(app: MainApplication)
    override fun inject(instance: DaggerApplication?)

    @Component.Builder
    interface Builder {
        @BindsInstance

        fun create(app: Application): Builder
        fun build(): AppComponent
    }
}