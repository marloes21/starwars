package com.example.starwars.data.spaceship

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.starwars.data.serialization.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
@Entity
data class Spaceship(
    @PrimaryKey
    val id: String,
    val name: String,
    val model: String,
    val manufacturer: String,
    val cost: String,
    val speed: String,
    val length: String,
    val crew: String,
    val passengers: String,
    val cargoCapacity: String,
    val consumables: String,
    val hyperdriveRating: String,
    val mglt: String,
)

@Serializable
data class SpaceshipDto(
    val name: String,
    val model: String,
    val manufacturer: String,
    @SerialName("cost_in_credits")
    val cost: String,
    val length: String,
    @SerialName("max_atmosphering_speed")
    val speed: String,
    val crew: String,
    val passengers: String,
    @SerialName("cargo_capacity")
    val cargoCapacity: String,
    val consumables: String,
    @SerialName("hyperdrive_rating")
    val hyperdriveRating: String,

    @SerialName("MGLT")
    val mglt: String,

    val films: List<String>,

    val url: String,

    @Serializable(with = LocalDateTimeSerializer::class)
    val created: LocalDateTime,

    @Serializable(with = LocalDateTimeSerializer::class)
    val edited: LocalDateTime,
) {

    fun toSpaceship(id: String): Spaceship {
        return Spaceship(
            id = id,
            name = name,
            model = model,
            manufacturer = manufacturer,
            cost = cost,
            speed = speed,
            length = length,
            crew = crew,
            passengers = passengers,
            cargoCapacity = cargoCapacity,
            consumables = consumables,
            hyperdriveRating = hyperdriveRating,
            mglt = mglt,
        )
    }
}