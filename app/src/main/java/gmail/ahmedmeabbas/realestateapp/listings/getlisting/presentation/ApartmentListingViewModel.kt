package gmail.ahmedmeabbas.realestateapp.listings.getlisting.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.listings.getlisting.data.GetListingRepository
import gmail.ahmedmeabbas.realestateapp.listings.models.Listing
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ApartmentListingUiState(
    val listing: Listing? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class ApartmentListingViewModel @Inject constructor(
    private val getListingRepository: GetListingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ApartmentListingUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeListing()
    }

    private fun observeListing() {
        viewModelScope.launch { 
            uiState.map { it.listing }.collect {
                Log.d(TAG, "observeListing: listing: $it")
            }
        }
    }

    fun getListingById(listingId: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getListingRepository.getListingById(listingId)
            getListingRepository.listingByIdFlow.collect { listing ->
                listing?.let {
                    _uiState.update {
                        it.copy(
                            listing = listing,
                            isLoading = false
                        )
                    }
                }
            }

        }
    }

    fun clearListing() {
        _uiState.update { it.copy(listing = null) }
    }

    companion object {
        private const val TAG = "ApartmentListingViewModel"
    }
}