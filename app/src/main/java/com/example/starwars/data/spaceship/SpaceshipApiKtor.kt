package com.example.starwars.data.spaceship

import com.example.starwars.data.KtorApi
import com.example.starwars.util.Resource
import io.ktor.client.HttpClient

class SpaceshipApiKtor(
    client: HttpClient,
    baseUrl: String,
) : SpaceshipApi, KtorApi(client, baseUrl) {

    override suspend fun getSpaceshipById(id: String): Resource<SpaceshipDto> =
        get<SpaceshipDto>(url = id)
}