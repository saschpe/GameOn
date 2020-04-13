package saschpe.gameon.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import saschpe.gameon.data.local.dao.FavoritesDao
import saschpe.gameon.data.local.model.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 3)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao

    companion object {
        internal const val DATABASE_NAME = "gameon.db"
    }
}