package com.ltu.m7019e.moviedb.v24.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ltu.m7019e.moviedb.v24.MovieDBApplication

class CachePopularMoviesWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    val context = ctx.applicationContext as MovieDBApplication
    override suspend fun doWork(): Result {
        return try {
            val popMovies = context.container.moviesRepository.getPopularMovies()

            // save each move to cache
            popMovies.results.forEach {
                it.cache = true
                context.container.savedMovieRepository.insertMovie(it)
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}