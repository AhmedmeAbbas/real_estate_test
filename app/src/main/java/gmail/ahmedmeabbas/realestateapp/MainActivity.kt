package gmail.ahmedmeabbas.realestateapp

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import gmail.ahmedmeabbas.realestateapp.databinding.ActivityMainBinding
import gmail.ahmedmeabbas.realestateapp.splashscreen.SplashScreenViewModel
import gmail.ahmedmeabbas.realestateapp.userpreferences.DataStoreKeys.KEY_APP_LANGUAGE
import gmail.ahmedmeabbas.realestateapp.userpreferences.UserPreferencesDataSourceImpl
import gmail.ahmedmeabbas.realestateapp.util.MyContextWrapper
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import java.util.*

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
        val userPreferencesDataSource = UserPreferencesDataSourceImpl(newBase!!)
        runBlocking {
            withTimeoutOrNull(1000) {
                setAppLanguage(userPreferencesDataSource.readString(KEY_APP_LANGUAGE))
            }
        }
        val language = appLanguage ?: Locale.getDefault().language

        super.attachBaseContext(MyContextWrapper(newBase).wrap(newBase, language))
    }
}