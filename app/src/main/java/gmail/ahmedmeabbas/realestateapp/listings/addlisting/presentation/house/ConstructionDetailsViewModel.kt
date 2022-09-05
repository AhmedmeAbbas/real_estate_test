package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.house

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.listings.addlisting.data.AddListingRepository
import gmail.ahmedmeabbas.realestateapp.listings.addlisting.domain.AddHouseUseCase
import javax.inject.Inject

@HiltViewModel
class ConstructionDetailsViewModel @Inject constructor(
    private val addListingRepository: AddListingRepository,
    private val addHouseUseCase: AddHouseUseCase
) : ViewModel() {

    fun getPropertyType() = addListingRepository.getPropertyType()

    fun addConstructionDetails(
        lotArea: Double?,
        builtArea: Double?,
        yearBuilt: Int?,
        structureType: String?,
        finishing: String?,
        moreInfo: String?
    ) {
        addHouseUseCase.addConstructionDetails(
            lotArea,
            builtArea,
            yearBuilt,
            structureType,
            finishing,
            moreInfo
        )
    }
}