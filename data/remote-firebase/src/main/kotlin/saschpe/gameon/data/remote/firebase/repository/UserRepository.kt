package saschpe.gameon.data.remote.firebase.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import saschpe.gameon.data.core.asResult

class UserRepository(private val firebaseAuth: FirebaseAuth) {
    suspend fun getUser() = asResult {
        firebaseAuth.currentUser ?: throw Exception("Unable to return user")
    }

    suspend fun createUserWithEmail(email: String, password: String) = asResult {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }

    suspend fun signInWithEmail(email: String, password: String) = asResult {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun signInWithGoogle(googleIdToken: String) = asResult {
        val credential = GoogleAuthProvider.getCredential(googleIdToken, null)
        firebaseAuth.signInWithCredential(credential).await()
    }

    suspend fun signOut() = asResult {
        firebaseAuth.signOut()
    }
}
