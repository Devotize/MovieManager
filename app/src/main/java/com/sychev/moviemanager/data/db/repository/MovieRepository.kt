package com.sychev.moviemanager.data.db.repository

import android.content.Context
import com.sychev.moviemanager.data.db.dao.MovieDao
import com.sychev.moviemanager.data.db.database.MovieDatabase
import com.sychev.moviemanager.data.db.entity.Movie

class MovieRepository(private val context: Context) {

    private var db: MovieDatabase = MovieDatabase.invoke(context)
    private var movieDao: MovieDao = db.movieDao()


    suspend fun addMovieToDatabase(movie: Movie) {
        movieDao.insertOrUpdateMovie(movie)
    }

    suspend fun getMoviesByStatus(status: String): List<Movie> {
        return movieDao.getMoviesByStatus(status)
    }

    suspend fun getAllMovies(): List<Movie> {
        return movieDao.getAllMovies()
    }

    suspend fun getMovieByName(name: String): Movie {
        return movieDao.getMovieByName(name)
    }

    suspend fun deleteMovie(movie: Movie) {
        movieDao.deleteMovie(movie)
    }

}