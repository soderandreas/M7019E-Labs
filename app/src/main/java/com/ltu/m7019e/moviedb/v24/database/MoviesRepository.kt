package com.ltu.m7019e.moviedb.v24.database

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ltu.m7019e.moviedb.v24.model.Genre
import com.ltu.m7019e.moviedb.v24.model.Movie
import com.ltu.m7019e.moviedb.v24.model.MovieDetailsResponse
import com.ltu.m7019e.moviedb.v24.model.MovieResponse
import com.ltu.m7019e.moviedb.v24.model.MovieReviewsResponse
import com.ltu.m7019e.moviedb.v24.model.MovieVideosResponse
import com.ltu.m7019e.moviedb.v24.network.MovieDBApiService
import com.ltu.m7019e.moviedb.v24.worker.CachePopularMoviesWorker
import com.ltu.m7019e.moviedb.v24.worker.CacheTopRatedMoviesWorker
import com.ltu.m7019e.moviedb.v24.worker.ClearCacheWorker
import java.util.concurrent.TimeUnit

interface MoviesRepository {
    suspend fun getPopularMovies(): MovieResponse
    suspend fun getTopRatedMovies(): MovieResponse
    suspend fun cachePopularMovies()
    suspend fun cacheTopRatedMovies()
    suspend fun getGenreMovies(genre: Genre): MovieResponse
    suspend fun getMovieInformation(id: Long): MovieDetailsResponse
    suspend fun getMovieReviews(id: Long): MovieReviewsResponse
    suspend fun getMovieVideos(id: Long): MovieVideosResponse
}

class NetworkMoviesRepository(context: Context, private val apiService: MovieDBApiService) : MoviesRepository {
    private val workManager = WorkManager.getInstance(context)
    val clearCacheBuilder = OneTimeWorkRequestBuilder<ClearCacheWorker>()
        .setInitialDelay(0.5.toLong(), TimeUnit.SECONDS)

    override suspend fun getPopularMovies(): MovieResponse {
        return apiService.getPopularMovies()
    }

    override suspend fun getTopRatedMovies(): MovieResponse {
        return apiService.getTopRatedMovies()
    }

    override suspend fun cachePopularMovies() {
        val cachePopMoviesBuilder = OneTimeWorkRequestBuilder<CachePopularMoviesWorker>()
            .setInitialDelay(0.5.toLong(), TimeUnit.SECONDS)
            .build()

        workManager
            .beginWith(clearCacheBuilder.build())
            .then(cachePopMoviesBuilder)
            .enqueue()
    }

    override suspend fun cacheTopRatedMovies() {
        val cacheTopMoviesBuilder = OneTimeWorkRequestBuilder<CacheTopRatedMoviesWorker>()
            .setInitialDelay(0.5.toLong(), TimeUnit.SECONDS)
            .build()

        workManager
            .beginWith(clearCacheBuilder.build())
            .then(cacheTopMoviesBuilder)
            .enqueue()
    }

    override suspend fun getGenreMovies(genre: Genre): MovieResponse {
        return apiService.getGenreMovies(genre.id)
    }

    override suspend fun getMovieInformation(id: Long): MovieDetailsResponse {
        return apiService.getMovieInformation(id)
    }

    override suspend fun getMovieReviews(id: Long): MovieReviewsResponse {
        return apiService.getMovieReviews(id)
    }

    override suspend fun getMovieVideos(id: Long): MovieVideosResponse {
        return apiService.getMovieVideos(id)
    }
}

interface SavedMovieRepository {
    suspend fun getSavedMovies(): List<Movie>

    suspend fun insertMovie(movie: Movie)

    suspend fun getMovie(id: Long): Movie

    suspend fun deleteMovie(movie: Movie)

    suspend fun getCache(): List<Movie>

    suspend fun cacheClear()
}

class FavoriteMoviesRepository(private val movieDao: MovieDao) : SavedMovieRepository {
    override suspend fun getSavedMovies(): List<Movie> {
        return movieDao.getFavoriteMovies()
    }

    override suspend fun insertMovie(movie: Movie) {
        movieDao.insertMovie(movie)
    }

    override suspend fun getMovie(id: Long): Movie {
        return movieDao.getMovie(id)
    }

    override suspend fun deleteMovie(movie: Movie) {
        movieDao.deleteFavoriteMovie(movie.id)
    }

    override suspend fun getCache(): List<Movie> {
        return movieDao.getCachedMovies()
    }

    override suspend fun cacheClear() {
        movieDao.deleteCache()
    }
}

/*interface ConnectionRepository {
    // save information after losing connection
    fun cacheInformation()
    // checks if connection to internet has been reestablished
    suspend fun checkConnection(): Boolean
}

class WorkManagerConnectionRepository(context: Context) : ConnectionRepository {
    private val workManager = WorkManager.getInstance(context)
    override fun cacheInformation() {
        //workManager.cancelUniqueWork("testname")
    }

    override suspend fun checkConnection(): Boolean {
        return isConnected()
    }

    private val connectivityManager = context.getSystemService(ConnectivityManager::class.java)

    // https://medium.com/@meytataliti/obtaining-network-connection-info-with-flow-in-android-af2e6b760dfd
    private fun isConnected(): Boolean {
        // Network class represents one of the networks that the device is connected to.
        val activeNetwork = connectivityManager.activeNetwork
        return if (activeNetwork == null) {
            false // if there is no active network, then simply no internet connection.
        } else {
            // NetworkCapabilities object contains information about properties of a network
            val netCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            (netCapabilities != null
                    // indicates that the network is set up to access the internet
                    && netCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    // indicates that the network provides actual access to the public internet when it is probed
                    && netCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED))
        }
    }
}*/
