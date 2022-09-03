package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.shared

import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.listings.addlisting.data.AddListingRepository
import javax.inject.Inject

@HiltViewModel
class AddPhotosViewModel @Inject constructor(
    private val addListingRepository: AddListingRepository
) : ViewModel() {

    fun getPropertyType() = addListingRepository.getPropertyType()

    fun addPhotos(photoUris: List<Uri?>) {
        addListingRepository.addPhotos(photoUris)
    }
}