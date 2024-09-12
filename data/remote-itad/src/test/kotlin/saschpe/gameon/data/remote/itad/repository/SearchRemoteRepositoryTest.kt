package saschpe.gameon.data.remote.itad.repository

import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.remote.itad.Api
import testing.Resources.getResourceString
import testing.headersContentTypeJson
import testing.mockHttpClient
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

private fun MockEngineConfig.searchApiHandler() = addHandler {
    when (val path = it.url.encodedPath) {
        "/v01/search/search" -> {
            when (val query = it.url.parameters["q"]) {
                null -> error("URL parameter 'q' missing")
                else -> when (val json = getResourceString("/testing/search/$query.json")) {
                    null -> error("Unhandled value for URL parameter 'q': $query")
                    else -> respond(json, HttpStatusCode.OK, headersContentTypeJson)
                }
            }
        }
        else -> error("Unhandled path: $path")
    }
}

class SearchRemoteRepositoryTest {
    private val api = spyk(
        Api(
            "GameOnTest",
            "123"
        )
    )
    private val repository = SearchRemoteRepository(api)

    init {
        // Arrange
        every { api.client } answers { mockHttpClient { searchApiHandler() } }
    }

    @Test
    fun search_noResults() = runBlocking {
        // Act, assert
        // TODO: coVerify { api.get(any(), any()) }
        when (val result = repository.search("no-results")) {
            is Result.Success<SearchRemoteRepository.SearchResponse> -> {
                assertEquals("USD", result.data.meta.currency)
                assertTrue(result.data.data.offers.isEmpty())
            }
            is Result.Error -> fail()
        }
    }

    @Test
    fun search_validResults() = runBlocking {
        // Act, assert
        // TODO: coVerify { api.get(any(), any()) }
        when (val result = repository.search("stellaris")) {
            is Result.Success<SearchRemoteRepository.SearchResponse> -> {
                assertEquals("USD", result.data.meta.currency)
                assertEquals(1, result.data.data.offers.size)
                assertEquals("stellaris", result.data.data.offers[0].plain)
                assertEquals("Stellaris", result.data.data.offers[0].title)
            }
            is Result.Error -> fail()
        }
    }
}
