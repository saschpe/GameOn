package saschpe.gameon.data.remote.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import saschpe.gameon.data.remote.firebase.repository.FavoritesRemoteRepository
import saschpe.gameon.data.remote.firebase.repository.UserRepository

object Module {
    private var firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFirestore = Firebase.firestore

    val favoritesRemoteRepository = FavoritesRemoteRepository(firebaseFirestore)
    val userRepository = UserRepository(firebaseAuth)
}