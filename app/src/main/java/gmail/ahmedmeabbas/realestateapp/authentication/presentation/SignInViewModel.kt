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
        viewModelScope.launch {
            authRepository.authMessagesFlow
                .filter { it.type == AuthMessageType.FACEBOOK_SIGN_IN }
                .collect { authMessage ->
                    _uiState.update { it.copy(userMessage = authMessage.message) }
                }
        }
    }

    fun handleFacebookAccessToken(accessToken: AccessToken) {
        viewModelScope.launch {
            authRepository.handleFacebookAccessToken(accessToken)
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(userMessage = "") }
    }
}