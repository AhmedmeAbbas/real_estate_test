package gmail.ahmedmeabbas.realestateapp.account.presentation

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.userpreferences.UserPreferencesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AccountUiState(
    val nightModeFlag: Int = AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
)

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    fun toggleNightMode(nightModeFlag: Int) {
        viewModelScope.launch {
            userPreferencesRepository.writeNightModeFlag(nightModeFlag)
            _uiState.update {
                it.copy(nightModeFlag =
                userPreferencesRepository.fetchNightModeFlag()
                    ?: AppCompatDelegate.MODE_NIGHT_UNSPECIFIED)
            }
        }
    }
}