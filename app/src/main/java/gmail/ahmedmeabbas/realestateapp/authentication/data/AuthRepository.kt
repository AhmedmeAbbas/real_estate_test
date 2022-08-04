package gmail.ahmedmeabbas.realestateapp.authentication.data

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {

    val userFlow: Flow<FirebaseUser?>
    val isUserSignedInFlow: StateFlow<Boolean>

    suspend fun signInWithEmailAndPassword(email: String, password: String): Boolean

    suspend fun signOut()

    suspend fun getCurrentUser(): FirebaseUser?
}