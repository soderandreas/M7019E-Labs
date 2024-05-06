package com.ltu.m7019e.moviedb.v24.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.ViewModel
import com.ltu.m7019e.moviedb.v24.model.Movie
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ltu.m7019e.moviedb.v24.MovieDBApplication
import com.ltu.m7019e.moviedb.v24.database.ConnectionRepository
import com.ltu.m7019e.moviedb.v24.database.MoviesRepository
import com.ltu.m7019e.moviedb.v24.database.SavedMovieRepository
import com.ltu.m7019e.moviedb.v24.model.Genre
import com.ltu.m7019e.moviedb.v24.model.MovieDetailsResponse
import com.ltu.m7019e.moviedb.v24.model.Review
import com.ltu.m7019e.moviedb.v24.model.Video
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

enum class MovieListName {
    GetPopular,
    GetTopRated,
}

sealed interface MovieListUiState {
    data class Success(val movies: List<Movie>) : MovieListUiState
    object Error : MovieListUiState
    object Loading : MovieListUiState
}

sealed interface SelectedMovieUiState {
    data class Success(
        val movie: Movie,
        val isFavorite: Boolean,
        val info: MovieDetailsResponse?,
        val reviews: List<Review>?,
        val videos: List<Video>?,
    ) : SelectedMovieUiState
    object Error : SelectedMovieUiState
    object Loading : SelectedMovieUiState
}

sealed interface SelectedGenreUiState {
    data class Success(val movies: List<Movie>, val genre: Genre) : SelectedGenreUiState
    object Error : SelectedGenreUiState
    object Loading : SelectedGenreUiState
}

class MovieDBViewModel(
    private val moviesRepository: MoviesRepository,
    private val savedMovieRepository: SavedMovieRepository,
    private val connectionRepository: ConnectionRepository
) : ViewModel() {

    var movieListUiState: MovieListUiState by mutableStateOf(MovieListUiState.Loading)
        private set

    var selectedMovieUiState: SelectedMovieUiState by mutableStateOf(SelectedMovieUiState.Loading)
        private set

    var selectedGenreUiState: SelectedGenreUiState by mutableStateOf(SelectedGenreUiState.Loading)
        private set



    private data class CachedMovieList(
        var listName: MovieListName? = null,
        var moviesList: List<Movie> = listOf()
    )

    private var cacheForList: CachedMovieList = CachedMovieList()

    init {
        getPopularMovies()
    }

    fun getTopRatedMovies() {
        viewModelScope.launch {
            if (cacheForList.listName == MovieListName.GetTopRated) {
                movieListUiState = MovieListUiState.Success(cacheForList.moviesList)
            } else if (connectionRepository.checkConnection()) {
                movieListUiState = MovieListUiState.Loading
                movieListUiState = try {
                    //MovieListUiState.Success(moviesRepository.getTopRatedMovies().results)
                    val movieList = moviesRepository.getTopRatedMovies()
                    cacheForList.moviesList = movieList.results
                    cacheForList.listName = MovieListName.GetTopRated
                    MovieListUiState.Success(movieList.results)
                } catch (e: IOException) {
                    MovieListUiState.Error
                } catch (e: HttpException) {
                    MovieListUiState.Error
                }
            } else {
                //Log.d("GETTOP", "No connection, no top rated movies")
                connectionRepository.checkConnection()
                movieListUiState = MovieListUiState.Error
                delay(5000)
                getTopRatedMovies()
            }
        }
    }

    fun getPopularMovies() {
        viewModelScope.launch {
            if (cacheForList.listName == MovieListName.GetPopular) {
                movieListUiState = MovieListUiState.Success(cacheForList.moviesList)
            } else if (connectionRepository.checkConnection()) {
                movieListUiState = MovieListUiState.Loading
                movieListUiState = try {
                    //MovieListUiState.Success(moviesRepository.getPopularMovies().results)
                    val movieList = moviesRepository.getPopularMovies()
                    cacheForList.moviesList = movieList.results
                    cacheForList.listName = MovieListName.GetPopular
                    MovieListUiState.Success(movieList.results)
                } catch (e: IOException) {
                    MovieListUiState.Error
                } catch (e: HttpException) {
                    MovieListUiState.Error
                }
            } else {
                //Log.d("GETPOP", "No connection, no popular movies")
                connectionRepository.checkConnection()
                movieListUiState = MovieListUiState.Error
                delay(5000)
                getPopularMovies()
            }
        }
    }

    fun getSavedMovies() {
        viewModelScope.launch {
            movieListUiState = MovieListUiState.Loading
            movieListUiState = try {
                MovieListUiState.Success(savedMovieRepository.getSavedMovies())
            } catch (e: IOException) {
                MovieListUiState.Error
            } catch (e: HttpException) {
                MovieListUiState.Error
            }
        }
    }

    fun saveMovie(movie: Movie) {
        viewModelScope.launch {
            savedMovieRepository.insertMovie(movie)
            selectedMovieUiState = SelectedMovieUiState.Success(
                movie,
                true,
                moviesRepository.getMovieInformation(movie.id),
                moviesRepository.getMovieReviews(movie.id).results,
                moviesRepository.getMovieVideos(movie.id).videos,
            )
        }
    }

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch {
            savedMovieRepository.deleteMovie(movie)
            selectedMovieUiState = SelectedMovieUiState.Success(
                movie,
                false,
                moviesRepository.getMovieInformation(movie.id),
                moviesRepository.getMovieReviews(movie.id).results,
                moviesRepository.getMovieVideos(movie.id).videos,
            )
        }
    }

    fun setSelectedMovie(movie: Movie) {
        viewModelScope.launch {
            selectedMovieUiState = SelectedMovieUiState.Loading
            selectedMovieUiState = try {
                SelectedMovieUiState.Success(
                    movie,
                    savedMovieRepository.getMovie(movie.id) != null,
                    moviesRepository.getMovieInformation(movie.id),
                    moviesRepository.getMovieReviews(movie.id).results,
                    moviesRepository.getMovieVideos(movie.id).videos,
                )
            } catch (e: UnknownHostException) {
                SelectedMovieUiState.Success(
                    movie,
                    savedMovieRepository.getMovie(movie.id) != null,
                    null,
                    null,
                    null,
                )
            } catch (e: HttpException) {
                SelectedMovieUiState.Error
            } catch (e: IOException) {
                SelectedMovieUiState.Error
            }
        }
    }

    fun setSelectedGenre(genre: Genre) {
        viewModelScope.launch {
            selectedGenreUiState = SelectedGenreUiState.Loading
            selectedGenreUiState = try {
                SelectedGenreUiState.Success(moviesRepository.getGenreMovies(genre).results, genre)
            } catch (e: IOException) {
                SelectedGenreUiState.Error
            } catch (e: HttpException) {
                SelectedGenreUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MovieDBApplication)
                val moviesRepository = application.container.moviesRepository
                val savedMovieRepository = application.container.savedMovieRepository
                val connectionRepository = application.container.connectionRepository
                MovieDBViewModel(
                    moviesRepository = moviesRepository,
                    savedMovieRepository = savedMovieRepository,
                    connectionRepository = connectionRepository
                )
            }
        }
    }
}