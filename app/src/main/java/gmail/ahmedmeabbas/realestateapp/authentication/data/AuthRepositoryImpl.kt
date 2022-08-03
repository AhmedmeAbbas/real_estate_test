package gmail.ahmedmeabbas.realestateapp.authentication.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    private var user: FirebaseUser? = null
    override val userFlow: Flow<FirebaseUser?> = flow {
        Log.d(TAG, "user flow: emitted")
        emit(auth.currentUser)
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmailAndPassword: auth success")
                } else {
                    Log.d(TAG, "signInWithEmailAndPassword: ${task.exception}")
                }
            }
    }

    companion object {
        private const val TAG = "AuthRepositoryImpl"
    }
}