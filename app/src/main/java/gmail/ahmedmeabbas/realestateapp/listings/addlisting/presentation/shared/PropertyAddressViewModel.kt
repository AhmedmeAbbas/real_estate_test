package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.shared

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.listings.addlisting.data.AddListingRepository
import javax.inject.Inject

@HiltViewModel
class PropertyAddressViewModel @Inject constructor(
    private val addListingRepository: AddListingRepository
) : ViewModel() {

    fun getPropertyType() = addListingRepository.getPropertyType()

    fun addPropertyAddress(
        state: String,
        city: String,
        region: String,
        block: Long,
        propertyNumber: Long
    ) {
        addListingRepository.addPropertyAddress(state, city, region, block, propertyNumber)
    }
}