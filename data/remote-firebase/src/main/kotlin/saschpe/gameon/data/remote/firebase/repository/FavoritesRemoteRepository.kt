package saschpe.gameon.data.remote.firebase.repository

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import saschpe.gameon.data.core.Result
import saschpe.gameon.data.core.asResult
import saschpe.gameon.data.core.model.Favorite
import java.util.*

class FavoritesRemoteRepository(
    private val db: FirebaseFirestore
) {
    suspend fun addUser(userId: String) = asResult {
        db.collection(USER_COLLECTION).add(userId).await()
    }

    /**
     * Schedule user for deletion.
     */
    suspend fun removeUser(userId: String) = asResult {
        db.collection(USER_COLLECTION).document(userId)
            .set(hashMapOf("delete" to true)).await()
    }

    suspend fun addOrUpdateFavorite(userId: String, favorite: Favorite) = asResult {
        db.collection(USER_COLLECTION).document(userId)
            .collection(USER_FAVORITES_COLLECTION).document(favorite.plain)
            .set(favorite.toHashMap()).await()
    }

    suspend fun getFavorites(userId: String) = asResult {
        db.collection(USER_COLLECTION).document(userId)
            .collection(USER_FAVORITES_COLLECTION).get().await().toObjects(Favorite::class.java)
    }

    @ExperimentalCoroutinesApi
    fun getFavoritesFlow(userId: String) = callbackFlow<Result<List<Favorite>>> {
        val listenerRegistration = db.collection(USER_COLLECTION).document(userId)
            .collection(USER_FAVORITES_COLLECTION)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    offer(Result.Error.withCause(exception.message, exception))
                    close(exception)
                } else if (snapshot != null && !snapshot.isEmpty) {
                    offer(Result.Success(snapshot.toObjects(Favorite::class.java)))
                }
            }
        awaitClose { listenerRegistration.remove() }
    }

    suspend fun removeFavorite(userId: String, favoriteId: String) = asResult {
        db.collection(USER_COLLECTION).document(userId)
            .collection(USER_FAVORITES_COLLECTION).document(favoriteId)
            .delete().await()
    }

    companion object {
        private const val USER_COLLECTION = "users"
        private const val USER_FAVORITES_COLLECTION = "favorites"
    }
}

fun Favorite.toHashMap() = hashMapOf(
    "id" to id,
    "plain" to plain,
    "createdAt" to Timestamp(Date(createdAt)),
    "priceThreshold" to priceThreshold
)