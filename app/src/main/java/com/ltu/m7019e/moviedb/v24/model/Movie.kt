package com.ltu.m7019e.moviedb.v24.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.Serial

@Serializable
@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey
    @SerialName(value = "id")
    var id: Long = 0L,

    @SerialName(value = "title")
    var title: String,

    @SerialName(value = "poster_path")
    var posterPath: String,

    @SerialName(value = "backdrop_path")
    var backdropPath: String,

    @SerialName(value = "release_date")
    var releaseDate: String,

    @SerialName(value = "overview")
    var overview: String,
)

@Serializable
data class Genre(
    @SerialName(value = "id")
    var id: Int,
    @SerialName(value = "name")
    var name: String
)

@Serializable
data class Review(
    @SerialName(value = "author")
    var author: String,
    @SerialName(value = "content")
    var content: String,
    @SerialName(value = "created_at")
    var createdAt: String,
    @SerialName(value = "url")
    var url: String
)

@Serializable
data class Video (
    @SerialName(value = "id")
    var id: String,
    @SerialName(value = "name")
    var name: String,
    @SerialName(value = "key")
    var key: String,
    @SerialName(value = "site")
    var site: String
)

/* @Serializable
data class MovieInformation(
    @SerialName(value = "genres")
    var genres: List<Genre>,
    @SerialName(value = "imdb_id")
    var imdbId: String? = null,
    @SerialName(value = "website")
    var website: String? = null,
) */

/* @Serializable
@Entity(tableName = "favorite_movies")
data class Movie(
    @PrimaryKey
    @SerialName(value = "id")
    var idMovie: Long = 0L,

    @SerialName(value = "title")
    var title: String,

    @SerialName(value = "poster_path")
    var posterPath: String,

    @SerialName(value = "backdrop_path")
    var backdropPath: String,

    @SerialName(value = "release_date")
    var releaseDate: String,

    @SerialName(value = "overview")
    var overview: String,

    @SerialName(value = "website")
    var website: String? = null,

    @SerialName(value = "imdb_id")
    var imdbId: String,
)


@Serializable
@Entity(tableName = "genres")
data class Genre(
    @PrimaryKey
    @SerialName(value = "id")
    var idGenre: Long = 0L,

    @SerialName(value = "name")
    var name: String,
)

@Serializable
@Entity(tableName = "movieGenre", primaryKeys = ["genre_id", "movie_id"])
data class MovieGenreCrossRed(
    // @SerialName(value = "genre_id")
    var idGenre: Long,

    // @SerialName(value = "movie_id")
    var idMovie: Long,
)

data class MovieWithGenre(
    @Embedded val movie: Movie,
    @Relation(
        parentColumn = "idMovie",
        entityColumn = "idGenre",
        associateBy = Junction(MovieGenreCrossRed::class)
    )
    val genres: List<Genre>
)*/
