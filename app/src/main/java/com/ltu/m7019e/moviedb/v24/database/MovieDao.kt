package com.ltu.m7019e.moviedb.v24.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ltu.m7019e.moviedb.v24.model.Movie

@Dao
abstract class MovieDao {
    @Query("SELECT * FROM movies")
    abstract suspend fun getFavoriteMovies(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertFavoriteMovie(movie: Movie)

    @Query("SELECT * FROM movies WHERE id = :id")
    abstract suspend fun getMovie(id: Long): Movie

    @Query("DELETE FROM movies WHERE id = :id")
    abstract fun deleteFavoriteMovie(id: Long)
}