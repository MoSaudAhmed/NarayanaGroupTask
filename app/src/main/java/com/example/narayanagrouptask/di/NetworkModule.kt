package com.example.narayanagrouptask.di

import com.example.narayanagrouptask.BuildConfig
import com.example.narayanagrouptask.network.ApiInterface
import com.example.narayanagrouptask.utils.BASE_URL
import dagger.Module
import dagger.Provides
import io.reactivex.annotations.NonNull
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@Suppress("unused")
class NetworkModule{

    @Provides
    @Singleton
    fun provideOkHttpInterceptors(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
    }

    @Provides
    @Singleton
    fun okHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {

        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                var request = chain.request().newBuilder()
                     .header("Content-Type", "application/json")
                     .header("Accept", "application/json")
                     .header("Accept-Language", "en-gb")
                    .build()
                return@addInterceptor chain.proceed(request = request)
            }.addInterceptor(httpLoggingInterceptor)
            .connectTimeout(60.toLong(), TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }


    @Provides
    @Singleton
    internal fun provideRetrofitInterface(@NonNull okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }

    @Provides
    @Singleton
    internal fun provideApiInterface(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

}
