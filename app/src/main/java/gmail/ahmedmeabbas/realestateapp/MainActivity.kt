package gmail.ahmedmeabbas.realestateapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import gmail.ahmedmeabbas.realestateapp.databinding.ActivityMainBinding
import gmail.ahmedmeabbas.realestateapp.splashscreen.SplashScreenViewModel
import gmail.ahmedmeabbas.realestateapp.util.DataStoreManager
import gmail.ahmedmeabbas.realestateapp.util.DataStoreManager.Companion.APP_LANGUAGE
import gmail.ahmedmeabbas.realestateapp.util.MyContextWrapper
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val splashScreenViewModel: SplashScreenViewModel by viewModels()
    private var appLanguage: String? = null

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
        appLanguage = newLang
    }

    override fun attachBaseContext(newBase: Context?) {
        val dataStoreManager = DataStoreManager(newBase!!)
        runBlocking {
            setAppLanguage(dataStoreManager.readString(APP_LANGUAGE))
        }
        val language = appLanguage ?: Locale.getDefault().language

        super.attachBaseContext(MyContextWrapper(newBase!!).wrap(newBase, language))
    }
}