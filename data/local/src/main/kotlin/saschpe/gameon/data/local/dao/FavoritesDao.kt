package saschpe.gameon.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import saschpe.gameon.data.local.model.FavoriteEntity

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorites")
    suspend fun getAll(): List<FavoriteEntity>

    @Query("SELECT * FROM favorites WHERE plain IN (:plains)")
    suspend fun getAllByPlains(plains: List<String>): List<FavoriteEntity>

    @Query("SELECT * FROM favorites WHERE plain = :plain")
    suspend fun getByPlain(plain: String): FavoriteEntity?

    @Insert
    suspend fun insert(entity: FavoriteEntity)

    @Delete
    suspend fun delete(entity: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE plain = :plain")
    suspend fun deleteByPlain(plain: String)
}