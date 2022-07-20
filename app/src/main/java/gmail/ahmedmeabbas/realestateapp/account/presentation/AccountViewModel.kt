package gmail.ahmedmeabbas.realestateapp.account.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.userpreferences.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AccountUiState(
    val isNightMode: Boolean = false
)

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    fun setNightMode(isNightMode: Boolean) {
        _uiState.update { uiState ->
            uiState.copy(isNightMode = isNightMode)
        }
    }

    fun toggleNightMode(nightModeOn: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.writeNightModeBoolean(nightModeOn)
            _uiState.update {
                it.copy(isNightMode = userPreferencesRepository.fetchNightModeBoolean() ?: return@launch)
            }
        }
    }
}