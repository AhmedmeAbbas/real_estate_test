package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.apartment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.listings.addlisting.data.AddListingRepository
import gmail.ahmedmeabbas.realestateapp.listings.addlisting.domain.AddApartmentUseCase
import javax.inject.Inject

@HiltViewModel
class UtilitiesApartmentViewModel @Inject constructor(
    private val addListingRepository: AddListingRepository,
    private val addApartmentUseCase: AddApartmentUseCase
) : ViewModel() {

    fun getPropertyType() = addListingRepository.getPropertyType()

    fun addApartmentUtilities(
        electricity: Boolean?,
        water: Boolean?,
        elevator: Boolean?,
        parking: Boolean?,
        backupGenerator: Boolean?,
        security: Boolean?,
        moreInfo: String?
    ) {
        addApartmentUseCase.addApartmentUtilities(
            electricity,
            water,
            elevator,
            parking,
            backupGenerator,
            security,
            moreInfo
        )
    }
}