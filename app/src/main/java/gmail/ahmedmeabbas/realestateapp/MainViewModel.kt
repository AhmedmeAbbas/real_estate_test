package gmail.ahmedmeabbas.realestateapp

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
    var savedLanguage: String? = null
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

    val savedLanguage = initialUserPrefs.map { it.language }

    init {
        fetchInitialState()
    }

    private fun fetchInitialState() {
        viewModelScope.launch {
            userPreferencesRepository.userPreferencesFlow
                .map { preferences ->
                preferences.language
            }.collect { language ->
                _uiState.update {
                    it.copy(savedLanguage = language)
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