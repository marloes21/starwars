package com.example.starwars.data.character

import com.example.starwars.util.Resource

interface CharacterApi {
    suspend fun getCharacterById(id: String) : Resource<CharacterDTO>
}