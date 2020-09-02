package com.sychev.moviemanager.data.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sychev.moviemanager.data.db.dao.MovieDao
import com.sychev.moviemanager.data.db.entity.Movie

@Database(entities = [Movie::class], version = 10)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var instance: MovieDatabase? = null
        private var LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also{ instance = it}
        }

        private fun buildDatabase(context: Context): MovieDatabase {
            return Room.databaseBuilder(context.applicationContext,
            MovieDatabase::class.java,
            "movie.db")
                .fallbackToDestructiveMigration()
                .build()
        }

    }

}