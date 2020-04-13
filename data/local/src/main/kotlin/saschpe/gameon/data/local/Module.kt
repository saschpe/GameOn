package saschpe.gameon.data.local

import androidx.room.Room
import saschpe.gameon.data.local.DataContentProvider.Companion.applicationContext
import saschpe.gameon.data.local.migrations.Migration1to2
import saschpe.gameon.data.local.migrations.Migration2to3
import saschpe.gameon.data.local.repository.FavoritesLocalRepository

object Module {
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .addMigrations(Migration1to2, Migration2to3)
            .build()
    }

    val favoritesLocalRepository = FavoritesLocalRepository(database.favoritesDao())
}