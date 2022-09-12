package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.shared

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.listings.addlisting.data.AddListingRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPhotosViewModel @Inject constructor(
    private val addListingRepository: AddListingRepository
) : ViewModel() {

    fun getPropertyType() = addListingRepository.getPropertyType()

    fun addPreviewPhotos(photoUris: List<Uri?>) {
        addListingRepository.addPreviewPhotos(photoUris)
    }

    fun uploadPhotos(contentResolver: ContentResolver, height: Int) {
        viewModelScope.launch {
            addListingRepository.getPhotoUrls(contentResolver, height)
        }
    }
}