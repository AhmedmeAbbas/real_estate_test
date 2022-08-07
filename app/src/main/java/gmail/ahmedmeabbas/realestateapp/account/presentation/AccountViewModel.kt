package gmail.ahmedmeabbas.realestateapp.account.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepository
import gmail.ahmedmeabbas.realestateapp.userpreferences.UserPreferencesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AccountUiState(
    var isUserSignedIn: Boolean = false,
    val displayName: String? = null
)

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    init {
        fetchInitialState()
        fetchDisplayName()
    }

    private fun fetchDisplayName() {
        viewModelScope.launch {
            authRepository.displayNameFlow.collect { displayName ->
                    _uiState.update {
                        it.copy(displayName = displayName)
                    }
                }
        }
    }

    private fun fetchInitialState() {
        viewModelScope.launch {
            authRepository.isUserSignedInFlow.collect { isSignedIn ->
                _uiState.update {
                    it.copy(isUserSignedIn = isSignedIn)
                }
            }
        }
    }

    fun toggleNightMode(nightModeFlag: Int) {
        viewModelScope.launch {
            userPreferencesRepository.writeNightModeFlag(nightModeFlag)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }
}