package gmail.ahmedmeabbas.realestateapp.account.profile.displayname

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepository
import gmail.ahmedmeabbas.realestateapp.authentication.util.AuthMessageType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DisplayNameDialogUiState(
    val displayName: String = "",
    val isLoading: Boolean = false,
    val userMessage: String = ""
)

@HiltViewModel
class DisplayNameViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DisplayNameDialogUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchDisplayName()
        observeMessages()
    }

    fun fetchDisplayName() {
        viewModelScope.launch {
            authRepository.userFlow
                .map { it?.displayName }
                .collect { displayName ->
                    _uiState.update {
                        it.copy(
                            displayName = displayName
                                ?: authRepository.userFlow.value?.email?.substringBefore("@")
                                ?: "",
                            isLoading = false
                        )
                    }
                }
        }
    }

    private fun observeMessages() {
        viewModelScope.launch {
            authRepository.authMessagesFlow
                .filter { it.type == AuthMessageType.DISPLAY_NAME }
                .collect { authMessage ->
                    _uiState.update {
                        it.copy(
                            userMessage = authMessage.message,
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

    fun clearMessages() {
        _uiState.update {
            it.copy(userMessage = "")
        }
    }

    companion object {
        private const val TAG = "DisplayNameViewModel"
    }
}