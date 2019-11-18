package saschpe.gameon.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import saschpe.gameon.data.local.model.WatchEntity

@Dao
interface WatchlistDao {
    @Query("SELECT * FROM watchlist")
    suspend fun getAll(): List<WatchEntity>

    @Query("SELECT * FROM watchlist WHERE plain = :plain")
    suspend fun getByPlain(plain: String): WatchEntity?

    @Insert
    suspend fun insert(entity: WatchEntity)

    @Delete
    suspend fun delete(entity: WatchEntity)

    @Query("DELETE FROM watchlist WHERE plain = :plain")
    suspend fun deleteByPlain(plain: String)
}