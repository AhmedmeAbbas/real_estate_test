package gmail.ahmedmeabbas.realestateapp.account.profile.displayname

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepository
import gmail.ahmedmeabbas.realestateapp.authentication.util.ErrorMessageType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DisplayNameDialogUiState(
    val displayName: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

@HiltViewModel
class DisplayNameViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DisplayNameDialogUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchDisplayName()
        observeErrorMessages()
    }

    private fun fetchDisplayName() {
        viewModelScope.launch {
            authRepository.displayNameFlow.collect { displayName ->
                _uiState.update {
                    it.copy(displayName = displayName ?: "", isLoading = false)
                }
            }
        }
    }

    private fun observeErrorMessages() {
        viewModelScope.launch {
            authRepository.errorMessageFlow
                .filter { it.type == ErrorMessageType.EMAIL_SIGN_IN }
                .collect { errorMessage ->
                    _uiState.update {
                        it.copy(
                            errorMessage = errorMessage.message,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun updateDisplayName(displayName: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            authRepository.updateDisplayName(displayName)
        }
    }
}