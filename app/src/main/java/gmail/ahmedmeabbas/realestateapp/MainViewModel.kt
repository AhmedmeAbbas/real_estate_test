package gmail.ahmedmeabbas.realestateapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gmail.ahmedmeabbas.realestateapp.userpreferences.DataStoreKeys.KEY_APP_LANGUAGE
import gmail.ahmedmeabbas.realestateapp.userpreferences.UserPreferencesRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

data class MainActivityUiState(
    val appLanguage: String = Locale.getDefault().language
)

class MainViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(MainActivityUiState())
    val uiState: StateFlow<MainActivityUiState> = _uiState.asStateFlow()

    private var fetchJob: Job? = null

    fun fetchAppLanguage() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            val savedLanguage = userPreferencesRepository.readString(KEY_APP_LANGUAGE)
            _uiState.update {
                it.copy(appLanguage = savedLanguage ?: Locale.getDefault().language)
            }
        }
    }
}