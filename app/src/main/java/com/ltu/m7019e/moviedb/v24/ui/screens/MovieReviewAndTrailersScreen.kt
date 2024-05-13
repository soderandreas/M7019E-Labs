package com.ltu.m7019e.moviedb.v24.ui.screens

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.ltu.m7019e.moviedb.v24.model.Review
import com.ltu.m7019e.moviedb.v24.model.Video
import com.ltu.m7019e.moviedb.v24.ui.theme.TheMovideDBV24Theme
import com.ltu.m7019e.moviedb.v24.utils.Constants.EXAMPLE_VIDEO_URI
import com.ltu.m7019e.moviedb.v24.viewmodel.MovieDBViewModel
import com.ltu.m7019e.moviedb.v24.viewmodel.SelectedGenreUiState
import com.ltu.m7019e.moviedb.v24.viewmodel.SelectedMovieUiState

@Composable
fun MovieReviewsAndTrailersScreen(
    movieDBViewModel: MovieDBViewModel,
    modifier: Modifier = Modifier,
) {
    val selectedMovieUiState = movieDBViewModel.selectedMovieUiState
    when (selectedMovieUiState) {
        is SelectedMovieUiState.Success -> {
            Column {
                if (!selectedMovieUiState.videos.isNullOrEmpty()) {
                    Text(
                        text = "Trailers and Videos",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                    LazyRow {
                        items(selectedMovieUiState.videos) { video ->
                            MovieTrailersAndScenesCard(video)
                        }
                    }
                }

                Spacer(modifier = Modifier.size(16.dp))

                if (!selectedMovieUiState.reviews.isNullOrEmpty()) {
                    Text(
                        text = "Reviews",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                    LazyRow {
                        items(selectedMovieUiState.reviews) { review ->
                            MovieReviewCard(
                                review,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(width = 260.dp, height = 140.dp)
                            )
                        }
                    }
                }
            }
        }
        is SelectedMovieUiState.Loading -> {
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }
        is SelectedMovieUiState.Error -> {
            Text(
                text = "Error...",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieReviewCard(review: Review, modifier: Modifier = Modifier){
    val mUriHandler = LocalUriHandler.current

    Card (
        modifier = modifier,
        onClick = { mUriHandler.openUri(review.url) }
    ) {
        Text(
            review.author,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(12.dp, 8.dp, 12.dp, 0.dp),
        )
        Text(
            review.content,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(8.dp),
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun MovieTrailersAndScenesCard(video: Video, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
    ) {
        Column {
            Text(
                text = video.name,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(8.dp)
            )
            ExoPlayerView(video)
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun ExoPlayerView(video: Video) {
    // Get the current context
    val context = LocalContext.current

    // Mutable state to control the visibility of the video title
    val visible = remember { mutableStateOf(true) }

    val url = "www.youtube.com/watch?v=" + video.key

    // Initialize ExoPlayer
    val exoPlayer = ExoPlayer.Builder(context).build()
        .apply {
            this.addListener(object : Player.Listener {
                override fun onEvents(player: Player, events: Player.Events) {
                    super.onEvents(player, events)
                    // Hide video title after playing for 200 milliseconds
                    if (player.contentPosition >= 200) visible.value = false
                    // When pressing play button
                    if (events.contains(Player.EVENT_PLAY_WHEN_READY_CHANGED)) {
                        Log.d("ExoPlayer", "Playing video")
                        Log.d("ExoPlayer", "YouTube video: $url")
                        if (video.site == "YouTube") {
                            val intentApp =
                                Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video.id))
                            try {
                                context.startActivity(intentApp)
                            } catch (ex: ActivityNotFoundException) {
                                Log.d("ExoPlayer", "No Youtube app found " + video.key)
                            }
                        }
                    }
                }
            })
        }

    // Create a MediaSource
    val mediaSource = remember(url) {
        MediaItem.fromUri(url)
    }

    // Set MediaSource to ExoPlayer
    LaunchedEffect(mediaSource) {
        exoPlayer.setMediaItem(mediaSource)
        exoPlayer.prepare()
    }

    // Manage lifecycle events
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Use AndroidView to embed an Android View (PlayerView) into Compose
    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Set your desired height
    )
}

@Preview(showBackground = true)
@Composable
fun MovieCardPreview(){
    TheMovideDBV24Theme {
        val reviews: List<Review> = listOf(
            Review("my name", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum", "", ""),
            Review("my name2", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum ", "", "")
        )
        LazyRow {
            items(reviews) { review ->
                MovieReviewCard(
                    review,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(width = 260.dp, height = 140.dp)
                )
            }
        }
    }
}

