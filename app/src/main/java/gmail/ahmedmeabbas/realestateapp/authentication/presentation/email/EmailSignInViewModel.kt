package gmail.ahmedmeabbas.realestateapp.authentication.presentation.email

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EmailSignInUiState(
    var signInSuccess: Boolean? = null
)

@HiltViewModel
class EmailSignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmailSignInUiState())
    val uiState: StateFlow<EmailSignInUiState> = _uiState.asStateFlow()

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch() {
            authRepository.signInWithEmailAndPassword(email, password)
        }
    }

    companion object {
        private const val TAG = "EmailSignInViewModel"
    }
}