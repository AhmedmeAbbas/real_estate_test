package gmail.ahmedmeabbas.realestateapp.mylistings.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepository
import gmail.ahmedmeabbas.realestateapp.listings.models.Listing
import javax.inject.Inject

@HiltViewModel
class MyListingsViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    val isUserSignedIn: LiveData<Boolean> = authRepository.isUserSignedInFlow.asLiveData()

    val myListingModels = listOf(Listing())
}