package saschpe.gameon.mobile.profile

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.toolbar
import kotlinx.android.synthetic.main.fragment_profile.*
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private lateinit var navController: NavController
    private var firebaseAuth = FirebaseAuth.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        setupWithNavController(toolbar, navController)
        toolbar.inflateMenu(R.menu.menu_home)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.help -> navController.navigate(R.id.action_profile_to_help)
                R.id.settings -> navController.navigate(R.id.action_profile_to_settings)
            }
            true
        }

        signIn.setOnClickListener {
            navController.navigate(
                R.id.action_profile_to_signIn, null, null,
                FragmentNavigatorExtras(signIn to getString(R.string.shared_element_signIn))
            )
        }
        signOut.setOnClickListener {
            firebaseAuth.signOut()
            updateSignInState()
        }
    }

    override fun onResume() {
        super.onResume()
        updateSignInState()
        firebaseAnalytics.setCurrentScreen(requireActivity(), "Profile", "ProfileFragment")
    }

    private fun updateSignInState() {
        if (firebaseAuth.currentUser != null && firebaseAuth.currentUser?.isAnonymous != true) {
            signIn.isVisible = false
            signOut.isVisible = true
        } else {
            signIn.isVisible = true
            signOut.isVisible = false
        }
    }
}