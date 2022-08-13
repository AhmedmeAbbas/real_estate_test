package gmail.ahmedmeabbas.realestateapp.authentication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.AccessToken
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepository
import gmail.ahmedmeabbas.realestateapp.authentication.util.AuthMessageType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SignInUiState(
    val isLoading: Boolean = false,
    val userMessage: String = ""
)

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeMessages()
    }

    private fun observeMessages() {
        val messageTypes = listOf(AuthMessageType.FACEBOOK_SIGN_IN, AuthMessageType.GOOGLE_SIGN_IN)
        viewModelScope.launch {
            authRepository.authMessagesFlow
                .filter { it.type in messageTypes }
                .collect { authMessage ->
                    _uiState.update { it.copy(userMessage = authMessage.message, isLoading = false) }
                }
        }
    }

    fun handleFacebookAccessToken(accessToken: AccessToken) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            authRepository.handleFacebookAccessToken(accessToken)
        }
    }

    fun handleGoogleAccessToken(idToken: String?) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            authRepository.handleGoogleAccessToken(idToken)
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(userMessage = "") }
    }
}