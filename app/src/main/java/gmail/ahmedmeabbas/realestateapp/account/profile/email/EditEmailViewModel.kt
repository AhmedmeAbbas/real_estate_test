package gmail.ahmedmeabbas.realestateapp.account.profile.email

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepository
import gmail.ahmedmeabbas.realestateapp.authentication.util.AuthMessageType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditEmailUiState(
    val currentEmail: String? = null,
    val isLoading: Boolean = false,
    val userMessage: String = ""
)

@HiltViewModel
class EditEmailViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditEmailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeMessages()
        fetchCurrentEmail()
    }

    private fun fetchCurrentEmail() {
        viewModelScope.launch {
            authRepository.userFlow
                .map { it?.email }
                .collect { email ->
                    _uiState.update {
                        it.copy(currentEmail = email)
                    }
                }
        }
    }

    private fun observeMessages() {
        viewModelScope.launch {
            authRepository.authMessagesFlow
                .filter { it.type == AuthMessageType.EDIT_EMAIL }
                .collect { authMessage ->
                    _uiState.update {
                        it.copy(userMessage = authMessage.message, isLoading = false)
                    }
                }
        }
    }

    fun updateEmail(email: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            authRepository.updateEmail(email)
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(userMessage = "") }
    }
}