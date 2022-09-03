package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.apartment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.listings.addlisting.data.AddListingRepository
import gmail.ahmedmeabbas.realestateapp.listings.addlisting.domain.AddApartmentUseCase
import javax.inject.Inject

@HiltViewModel
class ApartmentDetailsViewModel @Inject constructor(
    private val addListingRepository: AddListingRepository,
    private val addApartmentUseCase: AddApartmentUseCase
) : ViewModel() {

    fun getPropertyType() = addListingRepository.getPropertyType()

    fun addApartmentDetails(
        area: Double?,
        yearBuilt: Int?,
        finished: Boolean?,
        furnished: Boolean?,
        bedrooms: Int?,
        bathrooms: Int?,
        kitchen: Boolean?,
        livingRoom: Boolean?,
        balcony: Boolean?,
        floorNumber: Int?,
        moreInfo: String?
    ) {
        addApartmentUseCase.addApartmentDetails(
            area,
            yearBuilt,
            finished,
            furnished,
            bedrooms,
            bathrooms,
            kitchen,
            livingRoom,
            balcony,
            floorNumber,
            moreInfo
        )
    }
}