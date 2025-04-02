package com.example.starwars.data.character

import com.example.starwars.data.toNetworkError
import com.example.starwars.util.ErrorType
import com.example.starwars.util.Resource
import com.example.starwars.util.dataOrNull
import com.example.starwars.util.sync
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow


interface CharacterRepository {
    suspend fun getCharactersByIds(ids: List<String>): List<Character>
    suspend fun getCharacterById(id: String): Flow<Resource<Character>>
}

class CharacterRepositoryImpl(
    val api: CharacterApi,
    val dao: CharacterDao,
) : CharacterRepository {
    override suspend fun getCharactersByIds(ids: List<String>): List<Character> {
        //TODO how to handle not found characters?
        val characters = ids.mapNotNull { id ->
            getById(id)
        }
        sync({ Resource.Success(characters) }, dao::replaceAll)
        return characters
    }

    private suspend fun getById(id: String): Character? {
        val localCharacter = dao.getById(id)
        if (localCharacter != null) {
            return localCharacter
        } else {
            return api.getCharacterById(id).dataOrNull()?.toCharacter(id)
        }
    }

    override suspend fun getCharacterById(id: String): Flow<Resource<Character>> = flow {
        val character = getById(id)
        if (character == null) {
            emit(Resource.Failure(ErrorType.NoCharactersFound))
            return@flow
        }
        emit(Resource.Success(character))
    }.catch {
        emit(Resource.Failure(it.toNetworkError()))
    }
}
