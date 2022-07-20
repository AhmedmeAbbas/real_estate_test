package gmail.ahmedmeabbas.realestateapp

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.userpreferences.DataStoreKeys.KEY_APP_LANGUAGE
import gmail.ahmedmeabbas.realestateapp.userpreferences.UserPreferencesRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class MainActivityUiState(
    val savedLanguage: String? = null,
    val nightModeFlag: Int = AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(MainActivityUiState())
    val uiState: StateFlow<MainActivityUiState> = _uiState.asStateFlow()

    val initialUserPrefs = flow {
        emit(userPreferencesRepository.fetchInitialPreferences())
    }

    init {
        fetchInitialState()
    }

    private fun fetchInitialState() {
        viewModelScope.launch {
            userPreferencesRepository.userPreferencesFlow
                .collect { userPrefs ->
                _uiState.update {
                    it.copy(savedLanguage = userPrefs.language,
                        nightModeFlag = userPrefs.nightModeFlag
                    )
                }
            }
        }
    }

    /*
    private var fetchJob: Job? = null

    fun fetchAppLanguage() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            val savedLanguage = userPreferencesRepository.fetchAppLanguage()
            _uiState.update {
                it.copy(appLanguage = savedLanguage ?: Locale.getDefault().language)
            }
        }
    }
     */

    private fun mapUserPreferences() {

    }

}