package com.sychev.moviemanager.data.db.dao

import androidx.room.*
import com.sychev.moviemanager.data.db.entity.Movie

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<Movie>

    @Query("SELECT * FROM movies WHERE status = :status")
    suspend fun getMoviesByStatus(status: String): List<Movie>

    @Query("SELECT * FROM movies WHERE title = :name")
    suspend fun getMovieByName(name: String): Movie

//    @Query("SELECT * FROM movies WHERE id = :id")
//    suspend fun getMovieById(id: Int): Movie

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateMovie(movie: Movie)

    @Delete
    suspend fun deleteMovie(movie: Movie)

}