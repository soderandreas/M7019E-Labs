package com.ltu.m7019e.moviedb.v24.database

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ltu.m7019e.moviedb.v24.model.Movie

@Dao
abstract class MovieDao {
    @Query("SELECT * FROM movies WHERE favorite = 1")
    abstract suspend fun getFavoriteMovies(): List<Movie>

    @Query("SELECT * FROM movies WHERE cache = 1")
    abstract suspend fun getCachedMovies(): List<Movie>

    @Query("SELECT * FROM movies WHERE id = :id")
    abstract suspend fun getMovie(id: Long): Movie

    @Transaction
    open suspend fun insertMovie(movie: Movie) {
        /*
         * if movie is already in database, combine with new entry
         * otherwise insert movie
         */
        try {
            val tmp = getMovie(movie.id)

            movie.favorite = tmp.favorite || movie.favorite
            movie.cache = tmp.cache || movie.cache
        } catch (e: Exception){
            Log.d("Insert Movie Room", "Movie could not be found")
        }

        insertNewMovie(movie)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertNewMovie(movie: Movie)

    @Transaction
    open suspend fun deleteFavoriteMovie(id: Long) {
        // need to check if movie is in cache
        val tmp = getMovie(id)
        if (tmp.cache) {
            removeFavoriteStatus(id)
        } else {
            deleteMovie(id)
        }
    }

    @Query("UPDATE movies SET favorite = 0 WHERE id = :id")
    abstract suspend fun removeFavoriteStatus(id: Long)

    @Query("DELETE FROM movies WHERE id = :id")
    abstract suspend fun deleteMovie(id: Long)

    @Transaction
    open suspend fun deleteCache() {
        removeCacheStatusAll()
        deleteAllNonFavorite()
    }

    @Query("UPDATE movies SET cache = 0")
    abstract suspend fun removeCacheStatusAll()

    @Query("DELETE FROM movies WHERE favorite = 0")
    abstract suspend fun deleteAllNonFavorite()

}