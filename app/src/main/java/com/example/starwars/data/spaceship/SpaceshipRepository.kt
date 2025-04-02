package com.example.starwars.data.spaceship

import com.example.starwars.data.toNetworkError
import com.example.starwars.util.ErrorType
import com.example.starwars.util.Resource
import com.example.starwars.util.dataOrNull
import com.example.starwars.util.sync
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow


interface SpaceshipRepository {
    suspend fun getSpaceshipsByIds(ids: List<String>): List<Spaceship>
    suspend fun getSpaceshipById(id: String): Flow<Resource<Spaceship>>
}

class SpaceshipRepositoryImpl(
    val api: SpaceshipApi,
    val dao: SpaceshipDao,
) : SpaceshipRepository {
    override suspend fun getSpaceshipsByIds(ids: List<String>): List<Spaceship> {
        //TODO how to handle not found characters?
        val spaceShips = ids.mapNotNull { id ->
            getById(id)
        }
        sync({ Resource.Success(spaceShips) }, dao::replaceAll)
        return spaceShips
    }

    private suspend fun getById(id: String): Spaceship? {
        val localSpaceShip = dao.getById(id)
        if (localSpaceShip != null) {
            return localSpaceShip
        } else {
            return api.getSpaceshipById(id).dataOrNull()?.toSpaceship(id)
        }
    }

    override suspend fun getSpaceshipById(id: String): Flow<Resource<Spaceship>> = flow {
        val spaceShip = getById(id)
        if (spaceShip == null) {
            emit(Resource.Failure(ErrorType.NoCharactersFound))
            return@flow
        }
        emit(Resource.Success(spaceShip))
    }.catch {
        emit(Resource.Failure(it.toNetworkError()))
    }
}
