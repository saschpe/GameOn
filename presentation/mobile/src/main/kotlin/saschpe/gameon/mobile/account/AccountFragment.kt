package saschpe.gameon.mobile.account

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import kotlinx.android.synthetic.main.fragment_home.*
import saschpe.gameon.mobile.Module.firebaseAnalytics
import saschpe.gameon.mobile.R

class AccountFragment : Fragment(R.layout.fragment_account) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWithNavController(toolbar, findNavController())
        toolbar.inflateMenu(R.menu.menu_home)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.help -> findNavController().navigate(R.id.action_account_to_help)
                R.id.settings -> findNavController().navigate(R.id.action_account_to_settings)
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.setCurrentScreen(requireActivity(), "Account", "AccountFragment")
    }
}