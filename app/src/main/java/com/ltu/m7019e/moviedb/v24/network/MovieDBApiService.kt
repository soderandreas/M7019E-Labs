package com.ltu.m7019e.moviedb.v24.network

import com.ltu.m7019e.moviedb.v24.model.MovieDetailsResponse
import com.ltu.m7019e.moviedb.v24.model.MovieResponse
import com.ltu.m7019e.moviedb.v24.model.MovieReviewsResponse
import com.ltu.m7019e.moviedb.v24.model.MovieVideosResponse
import com.ltu.m7019e.moviedb.v24.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDBApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key")
        apiKey: String = Constants.API_KEY
    ): MovieResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key")
        apiKey: String = Constants.API_KEY
    ): MovieResponse

    @GET("movie/{Id}")
    suspend fun getMovieInformation(
        @Path("Id")
        id: Long,
        @Query("api_key")
        apiKey: String = Constants.API_KEY
    ): MovieDetailsResponse

    @GET("discover/movie")
    suspend fun getGenreMovies(
        @Query("with_genres")
        genre: Int,
        @Query("api_key")
        apiKey: String = Constants.API_KEY
    ): MovieResponse

    @GET("movie/{Id}/reviews")
    suspend fun getMovieReviews(
        @Path("Id")
        id: Long,
        @Query("api_key")
        apiKey: String = Constants.API_KEY
    ): MovieReviewsResponse

    @GET("movie/{Id}/videos")
    suspend fun getMovieVideos(
        @Path("Id")
        id: Long,
        @Query("api_key")
        apiKey: String = Constants.API_KEY
    ): MovieVideosResponse
}