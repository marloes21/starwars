package com.example.starwars.data.character

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.starwars.data.serialization.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
@Entity
data class Character(
    @PrimaryKey
    val id: String,
    val name: String,
    val height: String,
    val weight: String,
    val hairColor: String,
    val skinColor: String,
    val eyeColor: String,
    val birthYear: String,
    val gender: String,
    val homeWorld: String,
)

@Serializable
data class CharacterDTO(
    val name: String,
    val height: String,

    @SerialName("mass")
    val weight: String,

    @SerialName("hair_color")
    val hairColor: String,

    @SerialName("skin_color")
    val skinColor: String,

    @SerialName("eye_color")
    val eyeColor: String,

    @SerialName("birth_year")
    val birthYear: String,

    val gender: String,

    @SerialName("homeworld")
    val homeWorld: String,

    @SerialName("films")
    val filmsUrl: List<String>,

    @SerialName("species")
    val speciesUrl: List<String>,

    @SerialName("vehicles")
    val vehiclesUrl: List<String>,

    @SerialName("starships")
    val starShipsUrl: List<String>,

    @Serializable(with = LocalDateTimeSerializer::class)
    val created: LocalDateTime,

    @Serializable(with = LocalDateTimeSerializer::class)
    val edited: LocalDateTime,

    val url: String,
) {

    fun toCharacter(id: String): Character {
        return Character(
            id= id,
            name = name,
            height = height,
            weight = weight,
            hairColor = hairColor,
            skinColor = skinColor,
            eyeColor = eyeColor,
            birthYear = birthYear,
            gender = gender,
            homeWorld = homeWorld,
        )
    }
}