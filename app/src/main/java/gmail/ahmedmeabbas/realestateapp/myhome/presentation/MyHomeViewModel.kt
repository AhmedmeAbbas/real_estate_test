package gmail.ahmedmeabbas.realestateapp.myhome.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepository
import javax.inject.Inject

@HiltViewModel
class MyHomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    val isUserSignedIn: LiveData<Boolean> = authRepository.isUserSignedInFlow.asLiveData()
}