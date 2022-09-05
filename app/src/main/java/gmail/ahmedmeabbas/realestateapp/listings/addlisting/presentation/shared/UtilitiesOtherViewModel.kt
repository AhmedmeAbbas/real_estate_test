package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.shared

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.listings.addlisting.data.AddListingRepository
import gmail.ahmedmeabbas.realestateapp.listings.addlisting.domain.AddHouseUseCase
import javax.inject.Inject

@HiltViewModel
class UtilitiesOtherViewModel @Inject constructor(
    private val addListingRepository: AddListingRepository,
    private val addHouseUseCase: AddHouseUseCase
) : ViewModel() {

    fun getPropertyType() = addListingRepository.getPropertyType()

    fun addOtherUtilities(electricity: Boolean?, water: Boolean?, moreInfo: String?) {
        addHouseUseCase.addUtilities(electricity, water, moreInfo)
    }
}