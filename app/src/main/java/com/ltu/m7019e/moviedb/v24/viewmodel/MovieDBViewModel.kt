package com.ltu.m7019e.moviedb.v24.viewmodel

import androidx.lifecycle.ViewModel
import com.ltu.m7019e.moviedb.v24.database.MovieDBUiState
import com.ltu.m7019e.moviedb.v24.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MovieDBViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(MovieDBUiState())
    val uiState: StateFlow<MovieDBUiState> = _uiState.asStateFlow()

    fun setSelectedMovie(movie: Movie) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedGenre = null,
                selectedMovie = movie
            )
        }
    }

    fun setSelectedGenre(genre: String) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedGenre = genre
            )
        }
    }
}