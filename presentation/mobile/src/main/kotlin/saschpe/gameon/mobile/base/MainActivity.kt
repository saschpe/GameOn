package saschpe.gameon.mobile.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import saschpe.gameon.common.content.sharedPreferences
import saschpe.gameon.mobile.R

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController(R.id.navHostFragment)
        setupWithNavController(bottomNavigationView, navController)
    }

    override fun onStart() {
        super.onStart()
        bottomNavigationView.selectedItemId =
            sharedPreferences.getInt(PREF_SELECTED_ITEM, R.id.offersFragment)
    }

    override fun onStop() {
        sharedPreferences.edit {
            putInt(PREF_SELECTED_ITEM, bottomNavigationView.selectedItemId)
        }
        super.onStop()
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    companion object {
        private const val PREF_SELECTED_ITEM = "selected_item"
    }
}
