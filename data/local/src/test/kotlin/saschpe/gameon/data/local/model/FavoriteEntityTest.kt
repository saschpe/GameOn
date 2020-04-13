package saschpe.gameon.data.local.model

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import saschpe.gameon.data.local.AppDatabase
import saschpe.gameon.data.local.dao.FavoritesDao

@RunWith(AndroidJUnit4::class)
class FavoriteEntityTest {
    private lateinit var favoritesDao: FavoritesDao
    private lateinit var database: AppDatabase

    @Before
    fun createDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), AppDatabase::class.java
        ).build()
        favoritesDao = database.favoritesDao()
    }

    @After
    fun closeDatabase() = database.close()

    @Test
    fun getByPlain() = runBlocking {
        // Arrange
        favoritesDao.insert(TEST_ENTITY_ONE)

        // Act
        val byPlain = favoritesDao.getByPlain("Test")

        // Assert
        assertEquals(TEST_ENTITY_ONE, byPlain)
    }

    @Test
    fun getByMultipleByPlain() = runBlocking {
        // Arrange
        favoritesDao.insert(TEST_ENTITY_ONE)
        favoritesDao.insert(TEST_ENTITY_TWO)

        // Act
        val byPlains = favoritesDao.getAllByPlains(listOf("Test", "Bar"))

        // Assert
        assertEquals(2, byPlains.size)
        assertEquals(TEST_ENTITY_ONE, byPlains[0])
        assertEquals(TEST_ENTITY_TWO, byPlains[1])
    }

    @Test
    fun deleteAndGetByPlain() = runBlocking {
        // Arrange
        favoritesDao.insert(TEST_ENTITY_ONE)
        favoritesDao.delete(TEST_ENTITY_ONE)

        // Act
        val shouldBeNull = favoritesDao.getByPlain("Test")

        // Assert
        assertNull(shouldBeNull)
    }

    companion object {
        private val TEST_ENTITY_ONE = FavoriteEntity(id = 1, plain = "Test", priceThreshold = 3, dismissed = false)
        private val TEST_ENTITY_TWO = FavoriteEntity(id = 2, plain = "Bar", priceThreshold = 5, dismissed = true)
    }
}