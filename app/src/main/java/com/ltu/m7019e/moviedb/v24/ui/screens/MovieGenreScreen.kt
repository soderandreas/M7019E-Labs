package com.ltu.m7019e.moviedb.v24.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextDecoration
import com.ltu.m7019e.moviedb.v24.database.Movies
import com.ltu.m7019e.moviedb.v24.model.Movie
import com.ltu.m7019e.moviedb.v24.ui.theme.TheMovideDBV24Theme
import com.ltu.m7019e.moviedb.v24.viewmodel.SelectedGenreUiState
import kotlin.math.roundToInt

@Composable
fun MovieGenreScreen(
    selectedGenreUiState: SelectedGenreUiState,
    onMovieListItemClicked: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        val listState = rememberLazyListState()
        val topOfListProgress: Float by remember {
            derivedStateOf {
                listState.run {
                    if (firstVisibleItemIndex > 0) {
                        0f
                    } else {
                        layoutInfo.visibleItemsInfo
                            .firstOrNull()
                            ?.let {
                                1f - firstVisibleItemScrollOffset.toFloat() / it.size
                            } ?: 1f
                    }
                }
            }
        }

        when (selectedGenreUiState) {
            is SelectedGenreUiState.Success -> {
                Box (
                    modifier = Modifier
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)
                            val offset = (placeable.height * (1f - topOfListProgress)).roundToInt()
                            layout(placeable.width, placeable.height - offset) {
                                placeable.place(0, -offset)
                            }
                        }
                        .alpha(topOfListProgress)
                ) {
                    /*MovieGenreDescription(
                        genreText = selectedGenreUiState.genre.name,
                        modifier = modifier
                    )*/
                }

                Text(
                    text = selectedGenreUiState.genre.name,
                    style = MaterialTheme.typography.titleLarge,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center)
                        .padding(top = 4.dp)
                )

                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(top = 8.dp),
                    modifier = modifier.weight(1f)
                ) {
                    items(selectedGenreUiState.movies) { movie ->
                        MovieListItemCard(
                            movie = movie,
                            onMovieListItemClicked,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
            is SelectedGenreUiState.Loading -> {
                Text(
                    text = "Loading...",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp)
                )
            }
            is SelectedGenreUiState.Error -> {
                Text(
                    text = "Error: Something went wrong!",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

// Old Code
/*@Composable
fun MovieGenreDescription (
    genreText: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = genreText,
        fontStyle = FontStyle.Italic,
        color = Color.DarkGray,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun MovieScreenPreview() {
    TheMovideDBV24Theme {
        MovieGenreDescription(
            "Animation is a filmmaking technique by which still images are manipulated to create moving images. In traditional animation, images are drawn or painted by hand on transparent celluloid sheets (cels) to be photographed and exhibited on film. Animation has been recognized as an artistic medium, specifically within the entertainment industry. Many animations are computer animations made with computer-generated imagery (CGI). Stop motion animation, in particular claymation, has continued to exist alongside these other forms."
        )
    }
}*/

/* @Preview(showBackground = true)
@Composable
fun MovieScreenPreview2() {
    TheMovideDBV24Theme {
        MovieGenreScreen(
            "Animation is a filmmaking technique by which still images are manipulated to create moving images. In traditional animation, images are drawn or painted by hand on transparent celluloid sheets (cels) to be photographed and exhibited on film. Animation has been recognized as an artistic medium, specifically within the entertainment industry. Many animations are computer animations made with computer-generated imagery (CGI). Stop motion animation, in particular claymation, has continued to exist alongside these other forms.",
            Movies().getMovies().filter { it.genres.contains("Animation") },
            {},
            modifier = Modifier
                .padding(16.dp)
        )
    }
} */