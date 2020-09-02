package com.sychev.moviemanager.data.service

import com.sychev.moviemanager.data.response.MovieSearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieCall {
    private val movieService = MovieService.invoke()


    fun searchMovieByTerm(term: String,
    callback: (MovieSearchResponse?) -> Unit) {

        val  movieCall = movieService.getMovieBySearch(term)

        movieCall.enqueue(object : Callback<MovieSearchResponse>{

            override fun onFailure(call: Call<MovieSearchResponse>, t: Throwable) {
                callback(null)
            }

            override fun onResponse(
                call: Call<MovieSearchResponse>,
                response: Response<MovieSearchResponse>
            ) {
                println("debug Response: $response")
                val body = response.body()
                println("debug Body: $body")

                callback(body)

            }

        })


    }



}