package saschpe.gameon.mobile.profile

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_profile.*
import saschpe.gameon.data.core.Result
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        setupWithNavController(toolbar, navController)
        toolbar.inflateMenu(R.menu.menu_profile)
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
        signOut.setOnClickListener { viewModel.signOut() }

        connectToSteam.setOnClickListener {
            Snackbar.make(coordinatorLayout, getString(R.string.coming_soon), Snackbar.LENGTH_LONG)
                .show()
        }
        connectToIsThereAnyDeals.setOnClickListener {
            Snackbar.make(coordinatorLayout, getString(R.string.coming_soon), Snackbar.LENGTH_LONG)
                .show()
        }

        viewModel.userLiveData.observe(viewLifecycleOwner, Observer { result ->
            if (result is Result.Success<FirebaseUser> && !result.data.isAnonymous) {
                signIn.isVisible = false
                signOut.isVisible = true
            } else {
                signIn.isVisible = true
                signOut.isVisible = false
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUser()
        firebaseAnalytics.setCurrentScreen(requireActivity(), "Profile", "ProfileFragment")
    }
}