package gmail.ahmedmeabbas.realestateapp.authentication.presentation.email

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepository
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepositoryImpl
import gmail.ahmedmeabbas.realestateapp.authentication.domain.UserSignedInUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EmailSignInUiState(
    var signInSuccess: Boolean? = null
)

@HiltViewModel
class EmailSignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userSignedInUseCase: UserSignedInUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmailSignInUiState())
    val uiState: StateFlow<EmailSignInUiState> = _uiState.asStateFlow()

    init {
        fetchInitialState()
    }

    private fun fetchInitialState() {
        viewModelScope.launch {
            _uiState.update { uiState ->
                uiState.copy(signInSuccess = userSignedInUseCase.isUserSignedIn.first())
            }
            Log.d(TAG, "fetchInitialState: fetched")
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch() {
            authRepository.signInWithEmailAndPassword(email, password)
        }
    }

    companion object {
        private const val TAG = "EmailSignInViewModel"
    }
}