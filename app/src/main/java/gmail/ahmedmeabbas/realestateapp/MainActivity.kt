package gmail.ahmedmeabbas.realestateapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import gmail.ahmedmeabbas.realestateapp.databinding.ActivityMainBinding
import gmail.ahmedmeabbas.realestateapp.splash_screen.SplashScreenViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val splashScreenViewModel: SplashScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                splashScreenViewModel.isLoading.value
            }
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        val searchMenuItem = menu?.findItem(R.id.actionSearch)
        val searchView = searchMenuItem?.actionView as SearchView

        searchView.setIconifiedByDefault(false)
        searchView.queryHint = "Search city, area, address"
        searchView.background = resources.getDrawable(R.drawable.search_view_background, theme)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        val listMenuItem = menu.findItem(R.id.actionList)
        return super.onCreateOptionsMenu(menu)
    }
}