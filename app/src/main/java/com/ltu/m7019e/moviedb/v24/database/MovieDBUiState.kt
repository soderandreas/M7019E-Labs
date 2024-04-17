package com.ltu.m7019e.moviedb.v24.database

import com.ltu.m7019e.moviedb.v24.model.Movie

data class MovieDBUiState(
    val selectedMovie: Movie? = null,
    val selectedGenre: String? = null,
)
