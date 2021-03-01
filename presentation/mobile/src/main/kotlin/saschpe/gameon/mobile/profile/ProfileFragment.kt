package saschpe.gameon.mobile.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseUser
import saschpe.gameon.data.core.Result
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        setupWithNavController(binding.toolbar, navController)
        binding.toolbar.inflateMenu(R.menu.menu_profile)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.help -> navController.navigate(R.id.action_profile_to_help)
                R.id.settings -> navController.navigate(R.id.action_profile_to_settings)
            }
            true
        }

        binding.signIn.setOnClickListener {
            navController.navigate(
                R.id.action_profile_to_signIn, null, null,
                FragmentNavigatorExtras(binding.signIn to getString(R.string.shared_element_signIn))
            )
        }
        binding.signOut.setOnClickListener { viewModel.signOut() }

        binding.connectToSteam.setOnClickListener {
            Snackbar.make(binding.coordinatorLayout, getString(R.string.coming_soon), Snackbar.LENGTH_LONG)
                .show()
        }
        binding.connectToIsThereAnyDeals.setOnClickListener {
            Snackbar.make(binding.coordinatorLayout, getString(R.string.coming_soon), Snackbar.LENGTH_LONG)
                .show()
        }

        viewModel.userLiveData.observe(viewLifecycleOwner, { result ->
            if (result is Result.Success<FirebaseUser> && !result.data.isAnonymous) {
                binding.signIn.isVisible = false
                binding.signOut.isVisible = true
            } else {
                binding.signIn.isVisible = true
                binding.signOut.isVisible = false
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUser()
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Profile")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "ProfileFragment")
        }
    }
}