package gmail.ahmedmeabbas.realestateapp.authentication.data

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
): AuthRepository {

    override suspend fun signInWithEmailAndPassword(email: String, password: String) {
        TODO("Not yet implemented")
    }
}