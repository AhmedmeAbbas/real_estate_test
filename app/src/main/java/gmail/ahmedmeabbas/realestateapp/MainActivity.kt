package gmail.ahmedmeabbas.realestateapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import gmail.ahmedmeabbas.realestateapp.databinding.ActivityMainBinding
import gmail.ahmedmeabbas.realestateapp.splashscreen.SplashScreenViewModel
import gmail.ahmedmeabbas.realestateapp.util.MyContextWrapper
import gmail.ahmedmeabbas.realestateapp.userpreferences.UserPrefsEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val splashScreenViewModel: SplashScreenViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController
    private var appLanguage: String = Locale.getDefault().language

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
        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        Log.d(TAG, "onCreate: ${navController.currentDestination}")
        setUpBottomNavigationVisibilityListener()
        observeLanguageChange()
        observeNightModeChange()
    }

    private fun setUpBottomNavigationVisibilityListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.searchFragment -> showBottomNav()
                R.id.savedFragment -> showBottomNav()
                R.id.myHomeFragment -> showBottomNav()
                R.id.inboxFragment -> showBottomNav()
                R.id.accountFragment -> showBottomNav()
                R.id.signInFragment -> fadeOutBottomNav()
                else -> fadeOutBottomNav()
            }
        }
    }

    private fun fadeOutBottomNav() {
        binding.bottomNavigation.animate()
            .alpha(0f)
            .setDuration(300L)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    binding.bottomNavigation.visibility = View.GONE
                }
            })
    }

    private fun showBottomNav() {
        binding.bottomNavigation.apply {
            alpha = 1f
            visibility = View.VISIBLE
        }
    }

    private fun observeNightModeChange() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.uiState
                    .map { it.nightModeFlag }
                    .distinctUntilChanged()
                    .collect { nightModeFlag ->
                        toggleNightMode(nightModeFlag)
                    }
            }
        }
    }

    private fun toggleNightMode(nightModeFlag: Int) {
        AppCompatDelegate.setDefaultNightMode(nightModeFlag)
    }

    private fun observeLanguageChange() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.uiState
                    .map { it.savedLanguage }
                    .distinctUntilChanged()
                    .collect { savedLanguage ->
                        if (savedLanguage == appLanguage) {
                            return@collect
                        } else {
                            this@MainActivity.recreate()
                        }
                    }
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val userPrefsRepo = EntryPointAccessors.fromApplication(
            newBase!!, UserPrefsEntryPoint::class.java
        ).userPrefsRepo

        checkLocale()
        runBlocking {
            val userPrefs = userPrefsRepo.fetchInitialPreferences()
            val savedLanguage = userPrefs.language
            val nightModeFlag = userPrefs.nightModeFlag

            setAppLanguage(savedLanguage)
            toggleNightMode(nightModeFlag)
        }

        super.attachBaseContext(MyContextWrapper(newBase).wrap(newBase, appLanguage))
    }

    private fun checkLocale() {
        val supportedLanguages = listOf("ar", "en")
        if (Locale.getDefault().language !in supportedLanguages) {
            Locale.setDefault(Locale("en"))
        }
    }

    private fun setAppLanguage(newLang: String?) {
        newLang?.let {
            appLanguage = newLang
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}