package com.example.starwars.ui

import kotlinx.serialization.Serializable


@Serializable
sealed class MainScreen {

    @Serializable
    data object Films : MainScreen()

    @Serializable
    data class FilmDetail(
        val filmId: String,
        val filmTitle: String
    ): MainScreen()

    @Serializable
    data class CharacterDetail(
      val characterId: String,
        val name: String
    ): MainScreen()

    @Serializable
    data class SpaceshipDetail(
        val spaceshipId: String,
        val name: String
    ): MainScreen()
    companion object {
        val startDestination = Films
    }
}