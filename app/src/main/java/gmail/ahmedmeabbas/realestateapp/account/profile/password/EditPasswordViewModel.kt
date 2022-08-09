package gmail.ahmedmeabbas.realestateapp.account.profile.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepository
import gmail.ahmedmeabbas.realestateapp.authentication.util.AuthMessageType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditPasswordUiState(
    val isLoading: Boolean = false,
    val userMessage: String = ""
)

@HiltViewModel
class EditPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(EditPasswordUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeMessages()
    }

    private val messageTypes = listOf(AuthMessageType.RE_AUTHENTICATE, AuthMessageType.EDIT_PASSWORD)

    private fun observeMessages() {
        viewModelScope.launch {
            authRepository.authMessagesFlow
                .filter { it.type in messageTypes }
                .collect { authMessage ->
                    _uiState.update {
                        it.copy(userMessage = authMessage.message, isLoading = false)
                    }
                }
        }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            authRepository.updatePassword(password)
        }
    }

    fun reAuthenticateUser(email: String, password: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            authRepository.reAuthenticateUser(email, password)
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(userMessage = "") }
    }

    companion object {
        private const val TAG = "EditEmailViewModel"
    }
}