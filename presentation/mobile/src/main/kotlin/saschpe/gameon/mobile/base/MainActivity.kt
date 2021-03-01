package saschpe.gameon.mobile.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import saschpe.gameon.mobile.R
import saschpe.gameon.mobile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.App_DayNight)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = (supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment).navController
        setupWithNavController(binding.bottomNavigation, navController)

        initAdvertisements()
    }

    override fun onSupportNavigateUp() = navController.navigateUp()
}
