package saschpe.gameon.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import saschpe.gameon.data.local.dao.WatchlistDao
import saschpe.gameon.data.local.model.WatchEntity

@Database(entities = [WatchEntity::class], version = 1)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun watchlistDao(): WatchlistDao

    companion object {
        internal const val DATABASE_NAME = "gameon.db"
    }
}