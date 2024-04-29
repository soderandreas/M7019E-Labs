package com.ltu.m7019e.moviedb.v24.database

import com.ltu.m7019e.moviedb.v24.model.Genre
import com.ltu.m7019e.moviedb.v24.model.Movie
import com.ltu.m7019e.moviedb.v24.model.MovieDetailsResponse
import com.ltu.m7019e.moviedb.v24.model.MovieResponse
import com.ltu.m7019e.moviedb.v24.model.MovieReviewsResponse
import com.ltu.m7019e.moviedb.v24.model.MovieVideosResponse
import com.ltu.m7019e.moviedb.v24.network.MovieDBApiService

interface MoviesRepository {
    suspend fun getPopularMovies(): MovieResponse
    suspend fun getTopRatedMovies(): MovieResponse
    suspend fun getGenreMovies(genre: Genre): MovieResponse
    suspend fun getMovieInformation(id: Long): MovieDetailsResponse
    suspend fun getMovieReviews(id: Long): MovieReviewsResponse
    suspend fun getMovieVideos(id: Long): MovieVideosResponse
}

class NetworkMoviesRepository(private val apiService: MovieDBApiService) : MoviesRepository {
    override suspend fun getPopularMovies(): MovieResponse {
        return apiService.getPopularMovies()
    }

    override suspend fun getTopRatedMovies(): MovieResponse {
        return apiService.getTopRatedMovies()
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

}

class FavoriteMoviesRepository(private val movieDao: MovieDao) : SavedMovieRepository {
    override suspend fun getSavedMovies(): List<Movie> {
        return movieDao.getFavoriteMovies()
    }

    override suspend fun insertMovie(movie: Movie) {
        movieDao.insertFavoriteMovie(movie)
    }

    override suspend fun getMovie(id: Long): Movie {
        return movieDao.getMovie(id)
    }

    override suspend fun deleteMovie(movie: Movie) {
        movieDao.deleteFavoriteMovie(movie.id)
    }
}
