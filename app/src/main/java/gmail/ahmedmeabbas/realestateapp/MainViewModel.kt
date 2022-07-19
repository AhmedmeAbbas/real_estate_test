package gmail.ahmedmeabbas.realestateapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.userpreferences.DataStoreKeys.KEY_APP_LANGUAGE
import gmail.ahmedmeabbas.realestateapp.userpreferences.UserPreferencesRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class MainActivityUiState(
    val appLanguage: String = Locale.getDefault().language
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(MainActivityUiState())
    val uiState: StateFlow<MainActivityUiState> = _uiState.asStateFlow()

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
}