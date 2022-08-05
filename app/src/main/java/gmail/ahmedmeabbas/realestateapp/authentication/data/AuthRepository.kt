package gmail.ahmedmeabbas.realestateapp.authentication.data

import com.google.firebase.auth.FirebaseUser
import gmail.ahmedmeabbas.realestateapp.authentication.util.ErrorMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {

    val userFlow: Flow<FirebaseUser?>
    val isUserSignedInFlow: StateFlow<Boolean>
    val errorMessageFlow: SharedFlow<ErrorMessage>

    suspend fun signInWithEmailAndPassword(email: String, password: String)

    suspend fun signOut()

    suspend fun getCurrentUser(): FirebaseUser?
}