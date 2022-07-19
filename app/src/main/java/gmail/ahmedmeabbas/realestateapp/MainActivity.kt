package gmail.ahmedmeabbas.realestateapp

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import gmail.ahmedmeabbas.realestateapp.databinding.ActivityMainBinding
import gmail.ahmedmeabbas.realestateapp.splashscreen.SplashScreenViewModel
import gmail.ahmedmeabbas.realestateapp.util.MyContextWrapper
import gmail.ahmedmeabbas.realestateapp.util.UserPrefsEntryPoint
import kotlinx.coroutines.runBlocking
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val splashScreenViewModel: SplashScreenViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private var savedLanguage: String? = null

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

    private fun setAppLanguage(newLang: String?) {
        savedLanguage = newLang
    }

    override fun attachBaseContext(newBase: Context?) {
        val userPrefsRepo = EntryPointAccessors.fromApplication(
            newBase!!, UserPrefsEntryPoint::class.java
        )
            .userPrefsRepo

        runBlocking {
            setAppLanguage(userPrefsRepo.fetchAppLanguage())
        }
        val language = savedLanguage ?: Locale.getDefault().language
        super.attachBaseContext(MyContextWrapper(newBase).wrap(newBase, language))
    }
}