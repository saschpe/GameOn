package saschpe.gameon.data.local.repository

import saschpe.gameon.data.core.Result
import saschpe.gameon.data.local.dao.WatchlistDao
import saschpe.gameon.data.local.model.WatchEntity

class WatchlistLocalRepository(
    private val watchlistDao: WatchlistDao
) {
    suspend fun getAll(): Result<List<WatchEntity>> {
        val results = watchlistDao.getAll()
        return Result.Success(results)
    }

    suspend fun insert(entity: WatchEntity): Result<Unit> {
        watchlistDao.insert(entity)
        return Result.Success(Unit)
    }

    suspend fun delete(entity: WatchEntity): Result<Unit> {
        watchlistDao.delete(entity)
        return Result.Success(Unit)
    }

    suspend fun deleteByPlain(plain: String): Result<Unit> {
        watchlistDao.deleteByPlain(plain)
        return Result.Success(Unit)
    }
}