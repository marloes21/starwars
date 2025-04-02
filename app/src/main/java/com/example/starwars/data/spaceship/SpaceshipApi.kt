package com.example.starwars.data.spaceship

import com.example.starwars.util.Resource

interface SpaceshipApi {
    suspend fun getSpaceshipById(id: String) : Resource<SpaceshipDto>
}