package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.house

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.listings.addlisting.data.AddListingRepository
import javax.inject.Inject

@HiltViewModel
class HousePreviewViewModel @Inject constructor(
    private val addListingRepository: AddListingRepository
) : ViewModel() {

    fun getHouse() = addListingRepository.getListing()

    fun getPhotoUris() = addListingRepository.getPreviewPhotos()
}