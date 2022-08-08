package gmail.ahmedmeabbas.realestateapp.authentication.presentation.email

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepository
import gmail.ahmedmeabbas.realestateapp.authentication.util.ErrorMessageType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EmailSignInUiState(
    var errorMessage: String = "",
    var isLoading: Boolean = false
)

@HiltViewModel
class EmailSignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmailSignInUiState())
    val uiState: StateFlow<EmailSignInUiState> = _uiState.asStateFlow()

    val isUserSignedIn: LiveData<Boolean> = authRepository.isUserSignedInFlow.asLiveData()

    init {
        observeErrorMessages()
    }

    private fun observeErrorMessages() {
        viewModelScope.launch {
            authRepository.errorMessageFlow
                .filter { it.type == ErrorMessageType.EMAIL_SIGN_IN }
                .collect { errorMessage ->
                    _uiState.update { it.copy(
                        errorMessage = errorMessage.message,
                        isLoading = false
                    ) }
                }
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            authRepository.signInWithEmailAndPassword(email, password)
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = "") }
    }

    fun stopLoading() {
        _uiState.update { it.copy(isLoading = false) }
    }

    companion object {
        private const val TAG = "EmailSignInViewModel"
    }
}