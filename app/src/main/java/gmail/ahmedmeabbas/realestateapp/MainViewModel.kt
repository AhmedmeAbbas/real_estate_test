package gmail.ahmedmeabbas.realestateapp

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.userpreferences.UserPreferencesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class MainActivityUiState(
    val savedLanguage: String = Locale.getDefault().language,
    val nightModeFlag: Int = AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(MainActivityUiState())
    val uiState: StateFlow<MainActivityUiState> = _uiState.asStateFlow()

    init {
        fetchInitialState()
    }

    fun fetchInitialState() {
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
}