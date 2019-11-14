package saschpe.gamesale.data.remote.repository

import io.ktor.client.features.ResponseException
import kotlinx.serialization.json.JsonDecodingException
import saschpe.gamesale.data.core.Result

interface RemoteRepository {
    /**
     * Wraps a Ktor HTTP client response in a LunchUp [Result].
     *
     * [ResponseException] and it's sub-classes are converted to [Result.Error].
     */
    suspend fun <T : Any> asResult(lambda: suspend () -> T): Result<T> = try {
        Result.Success(lambda())
    } catch (exception: JsonDecodingException) {
        Result.Error.withCause(exception.message, exception)
    } catch (exception: ResponseException) {
        Result.Error.withCause(exception.message, exception)
    }
}
