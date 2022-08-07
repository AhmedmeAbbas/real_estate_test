package gmail.ahmedmeabbas.realestateapp

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepository
import gmail.ahmedmeabbas.realestateapp.userpreferences.UserPreferencesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class MainActivityUiState(
    val savedLanguage: String = Locale.getDefault().language,
    val nightModeFlag: Int = AppCompatDelegate.MODE_NIGHT_UNSPECIFIED,
    val isUserSignedIn: Boolean = false
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val authRepository: AuthRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(MainActivityUiState())
    val uiState: StateFlow<MainActivityUiState> = _uiState.asStateFlow()

    init {
        fetchUserPrefs()
        fetchSignInState()
    }

    private fun fetchUserPrefs() {
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

    private fun fetchSignInState() {
        viewModelScope.launch {
            authRepository.isUserSignedInFlow.collect { isSignedIn ->
                _uiState.update {
                    it.copy(isUserSignedIn = isSignedIn)
                }
            }
        }
    }
}