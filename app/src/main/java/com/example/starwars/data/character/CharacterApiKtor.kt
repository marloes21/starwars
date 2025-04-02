package com.example.starwars.data.character

import com.example.starwars.data.KtorApi
import com.example.starwars.util.Resource
import io.ktor.client.HttpClient

class CharacterApiKtor(
    client: HttpClient,
    baseUrl: String,
) : CharacterApi, KtorApi(client, baseUrl) {

    override suspend fun getCharacterById(id: String): Resource<CharacterDTO> =
        get<CharacterDTO>(url = id)
}