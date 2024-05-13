package com.ltu.m7019e.moviedb.v24

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ltu.m7019e.moviedb.v24.network.NetworkStatus
import com.ltu.m7019e.moviedb.v24.ui.screens.MovieDetailScreen
import com.ltu.m7019e.moviedb.v24.ui.screens.MovieGenreScreen
import com.ltu.m7019e.moviedb.v24.ui.screens.MovieListGridScreen
import com.ltu.m7019e.moviedb.v24.ui.screens.MovieReviewsAndTrailersScreen
import com.ltu.m7019e.moviedb.v24.viewmodel.MovieDBViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

enum class MovieDBScreen(@StringRes val title: Int) {
    List(title = R.string.app_name),
    Detail(title = R.string.movie_Detail),
    Review(title = R.string.movie_Review),
    Genre(title = R.string.movie_Genre)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDBAppBar(
    currentScreen: MovieDBScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    movieDBViewModel: MovieDBViewModel
) {
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        actions = {
            IconButton(onClick = {
                // Set the menu expanded state to the opposite of the current state
                menuExpanded = !menuExpanded
            }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Open Menu to select different movie lists"
                )
            }
            DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                DropdownMenuItem(
                    onClick = {
                        // Set the selected movie list to popular
                        movieDBViewModel.getPopularMovies()
                        // Set the menu expanded state to false
                        menuExpanded = false

                    },
                    text = {
                        Text(stringResource(R.string.popular_movies))
                    }
                )
                DropdownMenuItem(
                    onClick = {
                        // Set the selected movie list to popular
                        movieDBViewModel.getTopRatedMovies()
                        // Set the menu expanded state to false
                        menuExpanded = false

                    },
                    text = {
                        Text(stringResource(R.string.top_rated_movies))
                    }
                )
                DropdownMenuItem(
                    onClick = {
                        // Set the selected movie list to popular
                        movieDBViewModel.getSavedMovies()
                        // Set the menu expanded state to false
                        menuExpanded = false

                    },
                    text = {
                        Text(stringResource(R.string.saved_movies))
                    }
                )
            }
        },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@ExperimentalCoroutinesApi
@Composable
fun connectivityState(movieDBViewModel: MovieDBViewModel): State<NetworkStatus> {
    // Creates a State<ConnectionState> with current connectivity state as initial value
    return produceState(initialValue = movieDBViewModel.currentConnectivityState()) {
        // In a coroutine, can make suspend calls
        movieDBViewModel.observeConnectivityState().collect {

            value = movieDBViewModel.currentConnectivityState()
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun TheMovieDBApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = MovieDBScreen.valueOf(
        backStackEntry?.destination?.route ?: MovieDBScreen.List.name
    )

    val movieDBViewModel: MovieDBViewModel = viewModel(factory = MovieDBViewModel.Factory)

    val connection by connectivityState(movieDBViewModel)

    Log.d("NETWORK TEST", "CURRENT STATUS: $connection")

    if (connection == NetworkStatus.Connected) {
        movieDBViewModel.updateListUiState()
    }

    Scaffold(
        topBar = {
            MovieDBAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                movieDBViewModel = movieDBViewModel
            )
        }
    ) { innerPadding ->
        // val uiState by viewModel.uiState.collectAsState()
        NavHost(
            navController = navController,
            startDestination = MovieDBScreen.List.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = MovieDBScreen.List.name) {
                MovieListGridScreen(
                    movieDBViewModel = movieDBViewModel,
                    onMovieListItemClicked = { movie ->
                        movieDBViewModel.setSelectedMovie(movie)
                        navController.navigate(MovieDBScreen.Detail.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }
            composable(route = MovieDBScreen.Detail.name) {
                MovieDetailScreen(
                    movieDBViewModel = movieDBViewModel,
                    onGenreListItemClicked = { genre ->
                        movieDBViewModel.setSelectedGenre(genre)
                        navController.navigate(MovieDBScreen.Genre.name)
                    },
                    onReviewItemClicked = {
                        navController.navigate(MovieDBScreen.Review.name)
                    },
                    modifier = Modifier
                )
            }
            composable(route = MovieDBScreen.Review.name) {
                MovieReviewsAndTrailersScreen(
                    movieDBViewModel = movieDBViewModel,
                    modifier = Modifier,
                )
            }
            composable(route = MovieDBScreen.Genre.name) {
                Log.d("GenreScreen", "Opening genre screen")
                MovieGenreScreen(
                    selectedGenreUiState = movieDBViewModel.selectedGenreUiState,
                    onMovieListItemClicked = { movie ->
                        movieDBViewModel.setSelectedMovie(movie)
                        repeat(2) { navController.popBackStack() }
                        navController.navigate(MovieDBScreen.Detail.name)
                    },
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
        }

    }
}
