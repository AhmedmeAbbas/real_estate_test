package gmail.ahmedmeabbas.realestateapp.authentication.data

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val userFlow: Flow<FirebaseUser?>

    suspend fun signInWithEmailAndPassword(email: String, password: String)

    suspend fun signOut()
}