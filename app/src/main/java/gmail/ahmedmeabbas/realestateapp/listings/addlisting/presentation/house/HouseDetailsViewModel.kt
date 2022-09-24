package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.house

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.listings.addlisting.data.AddListingRepository
import gmail.ahmedmeabbas.realestateapp.listings.addlisting.domain.AddHouseUseCase
import javax.inject.Inject

@HiltViewModel
class HouseDetailsViewModel @Inject constructor(
    private val addListingRepository: AddListingRepository,
    private val addHouseUseCase: AddHouseUseCase
) : ViewModel() {

    fun getPropertyType() = addListingRepository.getPropertyType()

    fun addHouseDetails(
        bedrooms: Long?,
        bathrooms: Long?,
        kitchens: Long?,
        halls: Long?,
        floors: Long?,
        apartments: Boolean?,
        numberApartments: Long?,
        basement: Boolean?,
        garage: Boolean?,
        numberCars: Long?,
        moreInfo: String?
    ) {
        addHouseUseCase.addHouseDetails(
            bedrooms,
            bathrooms,
            kitchens,
            halls,
            floors,
            apartments,
            numberApartments,
            basement,
            garage,
            numberCars,
            moreInfo
        )
    }
}