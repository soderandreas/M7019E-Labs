package com.ltu.m7019e.moviedb.v24.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ltu.m7019e.moviedb.v24.R
import com.ltu.m7019e.moviedb.v24.model.Movie
import com.ltu.m7019e.moviedb.v24.ui.theme.TheMovideDBV24Theme
import com.ltu.m7019e.moviedb.v24.utils.Constants

@Composable
fun MovieDetailScreen(
    movie: Movie,
    onGenreListItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Box {
            AsyncImage(
                model = Constants.BACKDROP_IMAGE_BASE_URL + Constants.BACKDROP_IMAGE_WIDTH + movie.backdropPath,
                contentDescription = movie.title,
                modifier = modifier,
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = movie.title,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = movie.releaseDate,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = movie.overview,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.size(8.dp))

        Divider(modifier = modifier.padding(horizontal=8.dp))

        MovieDetailGenre(
            movie.genres,
            onGenreListItemClicked
        )

        Divider(modifier = modifier.padding(horizontal=8.dp))

        MovieButtonIMDB(movie)

        Divider(modifier = modifier.padding(horizontal=8.dp))

        val url = movie.website

        if (url != null) {
            MovieOfficialWebsite(url)
        }
    }
}

@Composable
fun MovieButtonIMDB(
    movie: Movie,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    val movieURL = stringResource(R.string.imdb_link) + movie.imdb_id

    Button(
        onClick = { uriHandler.openUri(movieURL) },
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.imdb_yellow),
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(10),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(stringResource(R.string.imdb))
    }

}

@Composable
fun MovieDetailGenre(
    genres: List<String>,
    onGenreListItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow () {
        items(genres) { genre ->
            Button(
                onClick = {
                    Log.d("MovieDetail", "Click on genre: $genre")
                    onGenreListItemClicked(genre)
                },
                modifier = modifier.padding(horizontal = 2.dp)
            ) {
                Text(genre)
            }
        }
    }
}

@Composable
fun MovieOfficialWebsite(
    officialURL: String
) {
    val annotatedText = buildAnnotatedString {
        append(stringResource(R.string.official_Website) + " ")

        pushStringAnnotation(
            tag = "URL",
            annotation = officialURL
        )
        withStyle(
            style = SpanStyle(
                color = Color.Blue, fontWeight = FontWeight.Bold
            )
        ) {
            val tmp = officialURL.substringAfter("//", "")
            append(tmp.substringBefore("/", tmp))
        }

        pop()
    }

    val mUriHandler = LocalUriHandler.current

    ClickableText(text = annotatedText, onClick = { offset ->
        annotatedText.getStringAnnotations(
            tag = "URL", start = offset, end = offset
        ).firstOrNull()?.let { annotation ->
            Log.d("Clicked URL", annotation.item)
            mUriHandler.openUri(annotation.item)
        }
    }, modifier = Modifier.padding(top = 24.dp))
}

@Preview(showBackground = true)
@Composable
fun MovieDetailPreview() {
    TheMovideDBV24Theme {
        MovieDetailScreen(
            movie = Movie(
                1,
                "Raya and the Last Dragon",
                "/lPsD10PP4rgUGiGR4CCXA6iY0QQ.jpg",
                "/9xeEGUZjgiKlI69jwIOi0hjKUIk.jpg",
                "2021-03-03",
                "Long ago, in the fantasy world of Kumandra, humans and dragons lived together in harmony. But when an evil force threatened the land, the dragons sacrificed themselves to save humanity. Now, 500 years later, that same evil has returned and it’s up to a lone warrior, Raya, to track down the legendary last dragon to restore the fractured land and its divided people.",
                listOf("Animation", "Family", "Fantasy", "Action", "Adventure"),
                "https://movies.disney.com/raya-and-the-last-dragon",
                "tt5109280"
            ), {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MovieDetailPreview2() {
    TheMovideDBV24Theme {
        MovieDetailScreen(
            movie = Movie(
                2,
                "Sentinelle",
                "/fFRq98cW9lTo6di2o4lK1qUAWaN.jpg",
                "/6TPZSJ06OEXeelx1U1VIAt0j9Ry.jpg",
                "2021-03-05",
                "Transferred home after a traumatizing combat mission, a highly trained French soldier uses her lethal skills to hunt down the man who hurt her sister.",
                listOf("Thriller", "Action", "Drama"),
                "https://www.netflix.com/title/81218770",
                "tt11734264"
            ), {}
        )
    }
}
