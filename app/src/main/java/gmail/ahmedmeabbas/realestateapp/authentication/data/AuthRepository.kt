package gmail.ahmedmeabbas.realestateapp.authentication.data

import com.google.firebase.auth.FirebaseUser
import gmail.ahmedmeabbas.realestateapp.authentication.util.AuthMessage
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {

    val userFlow: StateFlow<FirebaseUser?>
    val isUserSignedInFlow: StateFlow<Boolean>
    val authMessagesFlow: SharedFlow<AuthMessage>

    suspend fun signInWithEmailAndPassword(email: String, password: String)

    suspend fun signOut()

    suspend fun updateDisplayName(displayName: String)

    suspend fun reAuthenticateUser(email: String, password: String)

    suspend fun updateEmail(email: String)

    suspend fun updatePassword(password: String)

    suspend fun sendPasswordResetEmail(email: String)

    suspend fun createAccount(email: String, password: String)
}