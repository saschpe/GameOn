package saschpe.gameon.data.local.repository

import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.local.dao.FavoritesDao
import saschpe.gameon.data.local.model.FavoriteEntity

class FavoritesLocalRepositoryTest {
    private val favoritesDao = mockk<FavoritesDao>()
    private val repository = FavoritesLocalRepository(favoritesDao)

    @Test
    fun getAll() = runBlocking {
        // Arrange
        coEvery { favoritesDao.getAll() } returns listOf()

        // Act
        val result = repository.getAll()

        // Assert
        coVerify { favoritesDao.getAll() }
        when (result) {
            is Result.Success<List<FavoriteEntity>> -> assertTrue(result.data.isEmpty())
            is Result.Error -> fail()
        }
    }

    @Test
    fun getAllByPlains() = runBlocking {
        // Arrange
        coEvery { favoritesDao.getAllByPlains(any()) } returns listOf()

        // Act
        val result = repository.getAllByPlains(listOf("foo"))

        // Assert
        coVerify { favoritesDao.getAllByPlains(any()) }
        when (result) {
            is Result.Success<List<FavoriteEntity>> -> assertTrue(result.data.isEmpty())
            is Result.Error -> fail()
        }
    }

    @Test
    fun getByPlain_found() = runBlocking {
        // Arrange
        coEvery { favoritesDao.getByPlain(any()) } returns FavoriteEntity(
            plain = "foo",
            priceThreshold = 0
        )

        // Act
        val result = repository.getByPlain("foo")

        // Assert
        coEvery { favoritesDao.getByPlain(any()) }
        when (result) {
            is Result.Success<FavoriteEntity> -> assertEquals("foo", result.data.plain)
            is Result.Error -> fail()
        }
    }

    @Test
    fun getByPlain_notFound() = runBlocking {
        // Arrange
        coEvery { favoritesDao.getByPlain(any()) } returns null

        // Act
        val result = repository.getByPlain("foo")

        // Assert
        coEvery { favoritesDao.getByPlain(any()) }
        when (result) {
            is Result.Success<FavoriteEntity> -> fail()
            is Result.Error -> assertNotNull(result.throwable)
        }
    }

    @Test
    fun insert() = runBlocking {
        // Arrange
        coEvery { favoritesDao.insert(any()) } just Runs

        // Act
        val result = repository.insert(FavoriteEntity(plain = "foo", priceThreshold = 0, dismissed = true))

        // Assert
        coVerify { favoritesDao.insert(any()) }
        when (result) {
            is Result.Success<Unit> -> assertEquals(Unit, result.data)
            is Result.Error -> fail()
        }
    }

    @Test
    fun delete() = runBlocking {
        // Arrange
        coEvery { favoritesDao.delete(any()) } just Runs

        // Act
        val result = repository.delete(FavoriteEntity(plain = "foo", priceThreshold = 0))

        // Assert
        coVerify { favoritesDao.delete(any()) }
        when (result) {
            is Result.Success<Unit> -> assertEquals(Unit, result.data)
            is Result.Error -> fail()
        }
    }

    @Test
    fun deleteByPlain() = runBlocking {
        // Arrange
        coEvery { favoritesDao.deleteByPlain(any()) } just Runs

        // Act
        val result = repository.deleteByPlain("foo")

        // Assert
        coVerify { favoritesDao.deleteByPlain(any()) }
        when (result) {
            is Result.Success<Unit> -> assertEquals(Unit, result.data)
            is Result.Error -> fail()
        }
    }
}
