package com.example.w1832133

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// https://developer.android.com/training/data-storage/room/accessing-data
@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(vararg movie: Movie)

    @Query("SELECT * FROM movie")
    suspend fun getAll(): List<Movie>

    @Query("SELECT * FROM movie WHERE Actors LIKE '%' || :name || '%' ")
    suspend fun getSpecificMovieTitle(name: String): List<Movie>

}