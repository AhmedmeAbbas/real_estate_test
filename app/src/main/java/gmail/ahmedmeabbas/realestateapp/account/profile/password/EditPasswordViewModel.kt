package gmail.ahmedmeabbas.realestateapp.account.profile.password

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class EditPasswordUiState(
    val isLoading: Boolean = false,
    val userMessage: String = ""
)

@HiltViewModel
class EditPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(EditPasswordUiState())
    val uiState = _uiState.asStateFlow()


}