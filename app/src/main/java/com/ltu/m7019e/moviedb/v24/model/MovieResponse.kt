package com.ltu.m7019e.moviedb.v24.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse(
    @SerialName(value = "page")
    var page: Int = 0,
    @SerialName(value = "results")
    var results: List<Movie> = listOf(),
    @SerialName(value = "total_pages")
    var totalPages: Int = 0,
    @SerialName(value = "total_results")
    var totalResults: Int = 0,
)

@Serializable
data class MovieDetailsResponse(
    @SerialName(value = "genres")
    var genres: List<Genre>,
    @SerialName(value = "imdb_id")
    var imdbId: String? = null,
    @SerialName(value = "homepage")
    var website: String? = null,
)

@Serializable
data class MovieReviewsResponse(
    @SerialName(value = "id")
    var id: Long,
    @SerialName(value = "page")
    var page: Int,
    @SerialName(value = "results")
    var results: List<Review>,
    @SerialName(value = "total_pages")
    var totalPages: Int = 0,
    @SerialName(value = "total_results")
    var totalResults: Int = 0,
)

@Serializable
data class MovieVideosResponse(
    @SerialName(value = "id")
    var id: Long,
    @SerialName(value = "results")
    var videos: List<Video>
)
