package com.example.narayanagrouptask.network

import com.example.narayanagrouptask.models.HomeAllRepositoriesResponse
import com.example.narayanagrouptask.models.Owner
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiInterface {

    //repositories?q=page=1&per_page=10
    ///search/repositories?q=flutter

    @GET
    suspend fun getAllRepositories(
        @Url query: String,
        @Query("per_page") perPage: Int
    ): HomeAllRepositoriesResponse

    @GET("repos/{field1}/{field2}/contributors")
    fun getRepoDetails(
        @Path(value = "field1", encoded = false) field1: String,
        @Path(value = "field2", encoded = false) field2: String
    ): Single<List<Owner>>

    @GET("search/repositories?")
    fun getSearchRepositories(
        @Query("q") perPage: String
    ): Single<HomeAllRepositoriesResponse>
}