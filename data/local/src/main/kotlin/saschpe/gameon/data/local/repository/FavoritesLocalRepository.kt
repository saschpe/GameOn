package saschpe.gameon.data.local.repository

import saschpe.gameon.data.core.Result
import saschpe.gameon.data.local.dao.FavoritesDao
import saschpe.gameon.data.local.model.FavoriteEntity

class FavoritesLocalRepository(
    private val favoritesDao: FavoritesDao
) {
    suspend fun getAll(): Result<List<FavoriteEntity>> {
        val result = favoritesDao.getAll()
        return Result.Success(result)
    }

    suspend fun getAllByPlains(plains: List<String>): Result<List<FavoriteEntity>> {
        val result = favoritesDao.getAllByPlains(plains)
        return Result.Success(result)
    }

    suspend fun getByPlain(plain: String): Result<FavoriteEntity> {
        val result = favoritesDao.getByPlain(plain)
        return if (result != null) {
            Result.Success(result)
        } else {
            Result.Error.withMessage("Favorite with plain '${plain}' does not exist")
        }
    }

    suspend fun insert(entity: FavoriteEntity): Result<Unit> {
        favoritesDao.insert(entity)
        return Result.Success(Unit)
    }

    suspend fun delete(entity: FavoriteEntity): Result<Unit> {
        favoritesDao.delete(entity)
        return Result.Success(Unit)
    }

    suspend fun deleteByPlain(plain: String): Result<Unit> {
        favoritesDao.deleteByPlain(plain)
        return Result.Success(Unit)
    }
}