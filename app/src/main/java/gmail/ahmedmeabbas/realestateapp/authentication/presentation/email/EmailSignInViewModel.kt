package gmail.ahmedmeabbas.realestateapp.authentication.presentation.email

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepository
import gmail.ahmedmeabbas.realestateapp.authentication.data.ErrorMessageType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EmailSignInUiState(
    var errorMessage: String = ""
)

@HiltViewModel
class EmailSignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmailSignInUiState())
    val uiState: StateFlow<EmailSignInUiState> = _uiState.asStateFlow()

    init {
        fetchInitialState()
    }

    private fun fetchInitialState() {
        viewModelScope.launch {
            authRepository.errorMessageFlow
                .filter { it.type == ErrorMessageType.EMAIL_SIGN_IN }
                .collect { errorMessage ->
                    Log.d(TAG, "fetchInitialState: collect triggered")
                    _uiState.update { it.copy(errorMessage = errorMessage.message) }
                }
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            authRepository.signInWithEmailAndPassword(email, password)
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = "") }
    }

    companion object {
        private const val TAG = "EmailSignInViewModel"
    }
}