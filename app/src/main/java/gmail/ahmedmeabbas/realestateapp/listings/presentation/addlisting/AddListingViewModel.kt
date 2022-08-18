package gmail.ahmedmeabbas.realestateapp.listings.presentation.addlisting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.listings.data.Listing
import gmail.ahmedmeabbas.realestateapp.listings.data.ListingRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddListingViewModel @Inject constructor(
    private val listingRepo: ListingRepository
): ViewModel() {

    fun addListing(listing: Listing) {
        viewModelScope.launch {
            listingRepo.addListing(listing)
        }
    }

    fun getListing(area: String) {
        viewModelScope.launch {
            listingRepo.getListingsInArea(area)
        }
    }
}