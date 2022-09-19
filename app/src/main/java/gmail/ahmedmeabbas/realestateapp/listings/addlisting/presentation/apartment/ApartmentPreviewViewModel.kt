package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.apartment

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.listings.addlisting.data.AddListingRepository
import gmail.ahmedmeabbas.realestateapp.util.NetworkHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ApartmentPreviewUiState(
    val userMessage: String? = null,
    val uploadPhotosProgress: Int = 0
)

@HiltViewModel
class ApartmentPreviewViewModel @Inject constructor(
    private val addListingRepository: AddListingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ApartmentPreviewUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeMessages()
        observeUploadPhotosProgress()
    }

    fun getApartment() = addListingRepository.getListing()

    fun getPhotoUris() = addListingRepository.getPreviewPhotos()

    fun submitListing(contentResolver: ContentResolver, height: Int) {
        viewModelScope.launch {
            addListingRepository.submitListing(contentResolver, height)
        }
    }

    private fun observeMessages() {
        viewModelScope.launch {
            addListingRepository.addListingMessagesFlow.collect { message ->
                _uiState.update { it.copy(userMessage = message) }
            }
        }
    }

    private fun observeUploadPhotosProgress() {
        viewModelScope.launch {
            addListingRepository.uploadPhotosProgress.collect { progress ->
                _uiState.update { it.copy(uploadPhotosProgress = progress) }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(userMessage = null) }
    }

    fun isConnectionAvailable() = NetworkHelper.isConnectionAvailable
}