package com.example.starwars.data.film

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.starwars.data.serialization.LocalDateSerializer
import com.example.starwars.data.serialization.LocalDateTimeSerializer
import com.example.starwars.util.ext.getIdsFromUrl
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Serializable
data class Film(
    @PrimaryKey
    val id: String,
    val title: String,
    val episodeId: Int,
    val openingCrawl: String,
    val director: String,
    val producer: String,
    @Serializable(with = LocalDateSerializer::class)
    val releaseDate: LocalDate,
    val characterIds: List<String>,
    val spaceShipsIds: List<String>,
    val favorite: Boolean = false,
)

@Serializable
data class FilmDto(
    val title: String,

    @SerialName("episode_id")
    val episodeId: Int,

    @SerialName("opening_crawl")
    val openingCrawl: String,

    val director: String,

    val producer: String,

    @SerialName("release_date")
    @Serializable(with = LocalDateSerializer::class)
    val releaseDate: LocalDate,

    @SerialName("species")
    val speciesUrls: List<String>,

    @SerialName("starships")
    val starShipsUrls: List<String>,

    @SerialName("vehicles")
    val vehiclesUrls: List<String>,

    @SerialName("characters")
    val charactersUrls: List<String>,

    val planets: List<String>,

    val url: String,

    @Serializable(with = LocalDateTimeSerializer::class)
    val created: LocalDateTime,

    @Serializable(with = LocalDateTimeSerializer::class)
    val edited: LocalDateTime,
) {
    fun toFilm(): Film {
        return Film(
            title = title,
            episodeId = episodeId,
            openingCrawl = openingCrawl,
            director = director,
            producer = producer,
            releaseDate = releaseDate,
            spaceShipsIds = starShipsUrls.map {
                it.getIdsFromUrl()
            },
            characterIds = charactersUrls.map {
                it.getIdsFromUrl()
            },
            id = url.getIdsFromUrl()
        )
    }
}

@Serializable
data class FilmsDto(
    val count: Int,
    val next: String?,
    val previous: String?,
    @SerialName("results")
    val films: List<FilmDto>,
)