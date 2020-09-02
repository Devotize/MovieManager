package com.sychev.moviemanager.data.service

import com.google.gson.internal.bind.CollectionTypeAdapterFactory
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.sychev.moviemanager.data.response.MovieSearchResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.coroutines.CoroutineContext

const val API_KEY = "c28e7d7e"

//https://www.omdbapi.com/?apikey=c28e7d7e&t=La+La+Land&Leng=en

interface MovieService {
    @GET("?apikey=c28e7d7e&")
    fun getMovieBySearch(
        @Query("t") movieName: String,
        @Query("lang") languageCode: String = "en",
        @Query("plot") plotLength: String = "full"
    ): Call<MovieSearchResponse>

    companion object {
        operator fun invoke(): MovieService {
            return Retrofit.Builder()
                .baseUrl("https://www.omdbapi.com/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MovieService::class.java)
        }
    }

}