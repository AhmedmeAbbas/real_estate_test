package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepository
import gmail.ahmedmeabbas.realestateapp.listings.addlisting.data.AddListingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdvertiserInfoUiState(
    val userEmail: String? = null,
    val userId: String? = null
)

@HiltViewModel
class AdvertiserInfoViewModel @Inject constructor(
    private val addListingRepository: AddListingRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdvertiserInfoUiState())
    val uiState = _uiState.asStateFlow()

    fun getUserUID() {
        viewModelScope.launch {
            authRepository.userFlow.collect { user ->
                _uiState.update { it.copy(userId = user?.uid) }
            }
        }
    }

    fun getUserEmail() {
        viewModelScope.launch {
            authRepository.userFlow.collect { user ->
                _uiState.update { it.copy(userEmail = user?.email) }
            }
        }
    }

    fun getPropertyType() = addListingRepository.getPropertyType()

    fun addAdvertiserInfo(advertiserId: String, advertiser: String, phoneNumber: Int, email: String?) {
        addListingRepository.addAdvertiserInfo(advertiserId, advertiser, phoneNumber, email)
    }
}