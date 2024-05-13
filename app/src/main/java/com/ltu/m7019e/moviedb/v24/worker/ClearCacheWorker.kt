package com.ltu.m7019e.moviedb.v24.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ltu.m7019e.moviedb.v24.MovieDBApplication

class ClearCacheWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    val context = ctx.applicationContext as MovieDBApplication
    override suspend fun doWork(): Result {
        return try {
            context.container.savedMovieRepository.cacheClear()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}