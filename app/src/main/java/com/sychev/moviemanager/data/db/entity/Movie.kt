package com.sychev.moviemanager.data.db.entity

import androidx.room.*
import com.sychev.moviemanager.data.response.MovieStatus
import java.io.Serializable

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey val title: String,
    val year: String?,
    val released: String?,
    val runtime: String?,
    val genre: String?,
    val director: String?,
    val writer: String?,
    val actors: String?,
    val country: String?,
    val plot: String?,
    val awards: String?,
    val poster: String?,
    val imdbRating: String?,
    val boxOffice: String?,
    var status: String?, // can be watched and watchlist
    var userRating: String?
) : Serializable

