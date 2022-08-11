package gmail.ahmedmeabbas.realestateapp.authentication.presentation.createaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepository
import gmail.ahmedmeabbas.realestateapp.authentication.util.AuthMessageType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CreateAccountUiState(
    val isLoading: Boolean = false,
    val userMessage: String = ""
)

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(CreateAccountUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeMessages()
    }

    private fun observeMessages() {
        viewModelScope.launch {
            authRepository.authMessagesFlow
                .filter { it.type == AuthMessageType.CREATE_ACCOUNT }
                .collect { authMessage ->
                    _uiState.update {
                        it.copy(userMessage = authMessage.message, isLoading = false)
                    }
                }
        }
    }

    fun createAccount(email: String, password: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            authRepository.createAccount(email, password)
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(userMessage = "") }
    }
}