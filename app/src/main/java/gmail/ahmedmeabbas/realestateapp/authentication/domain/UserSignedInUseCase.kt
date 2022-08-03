package gmail.ahmedmeabbas.realestateapp.authentication.domain

import android.util.Log
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserSignedInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    val isUserSignedIn: Flow<Boolean> = flow {
        Log.d("UserSignedInUseCase", "use case: emitted")
        emit(authRepository.userFlow.first() != null)
    }
}