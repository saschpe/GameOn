package saschpe.gameon.mobile.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import saschpe.gameon.mobile.R

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.App_DayNight)
        super.onCreate(savedInstanceState)
        navController =
            (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment).navController
        setupWithNavController(bottomNavigationView, navController)
    }

    override fun onSupportNavigateUp() = navController.navigateUp()
}
