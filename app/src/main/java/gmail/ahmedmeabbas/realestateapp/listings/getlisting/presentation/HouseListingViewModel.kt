package gmail.ahmedmeabbas.realestateapp.listings.getlisting.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.listings.getlisting.data.GetListingRepository
import gmail.ahmedmeabbas.realestateapp.listings.models.Listing
import gmail.ahmedmeabbas.realestateapp.listings.models.Property
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HouseListingUiState(
    val isLoading: Boolean = true,
    val listing: Listing? = null
)

@HiltViewModel
class HouseListingViewModel @Inject constructor(
    private val getListingRepository: GetListingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HouseListingUiState())
    val uiState = _uiState.asStateFlow()

    fun getListingById(listingId: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getListingRepository.getListingById(listingId)
            getListingRepository.listingByIdFlow.collect { listing ->
                listing?.let {
                    if (listing.property !is Property.House) return@collect
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
}