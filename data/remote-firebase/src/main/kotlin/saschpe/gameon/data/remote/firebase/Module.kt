package saschpe.gameon.data.remote.firebase

import com.google.firebase.auth.FirebaseAuth
import saschpe.gameon.data.remote.firebase.repository.UserRepository

object Module {
    private var firebaseAuth = FirebaseAuth.getInstance()

    val userRepository = UserRepository(firebaseAuth)
}