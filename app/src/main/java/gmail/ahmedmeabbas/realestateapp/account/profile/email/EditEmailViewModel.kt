package gmail.ahmedmeabbas.realestateapp.account.profile.email

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepository
import gmail.ahmedmeabbas.realestateapp.authentication.util.AuthMessageType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditEmailUiState(
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
    }

    private val messageTypes = listOf(AuthMessageType.RE_AUTHENTICATE, AuthMessageType.EDIT_EMAIL)

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

    fun updateEmail(email: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            authRepository.updateEmail(email)
        }
    }

    fun reAuthenticateUser(email: String, password: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            Log.d(TAG, "reAuthenticateUser: re auth triggered")
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