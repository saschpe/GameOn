package saschpe.gameon.data.remote.itad.repository

import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.remote.itad.Api
import testing.Resources.getResourceString
import testing.headersContentTypeJson
import testing.mockHttpClient
import kotlin.test.assertEquals
import kotlin.test.fail

private fun MockEngineConfig.dealsApiHandler() = addHandler {
    when (val path = it.url.encodedPath) {
        "/v01/deals/list" -> {
            when (val json = getResourceString("/testing/deals/valid.json")) {
                null -> error("Unhandled value for URL parameter 'q': valid")
                else -> respond(json, HttpStatusCode.OK, headersContentTypeJson)
            }
        }
        else -> error("Unhandled path: $path")
    }
}

class DealsRemoteRepositoryTest {
    private val api = spyk(
        Api(
            "GameOnTest",
            "123"
        )
    )
    private val repository = DealsRemoteRepository(api)

    init {
        // Arrange
        every { api.client } answers { mockHttpClient { dealsApiHandler() } }
    }

    @Test
    fun deals() = runBlocking {
        // Act, assert
        // TODO: coVerify { api.get(any(), any()) }
        when (val result = repository.list()) {
            is Result.Success<DealsRemoteRepository.DealResponse> -> {
                assertEquals("USD", result.data.meta.currency)
                assertEquals(82239, result.data.data.count)
                assertEquals(2, result.data.data.offers.size)
                assertEquals("quantumleaper", result.data.data.offers[0].plain)
                assertEquals("Quantumleaper", result.data.data.offers[0].title)
                assertEquals("steam", result.data.data.offers[0].shop.id)
                assertEquals("Steam", result.data.data.offers[0].shop.name)
            }
            is Result.Error -> fail()
        }
    }
}