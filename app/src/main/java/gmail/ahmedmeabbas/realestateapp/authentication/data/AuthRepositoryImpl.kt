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

    private var user: FirebaseUser? = auth.currentUser
    override val userFlow: Flow<FirebaseUser?> = flow {
        Log.d(TAG, "user flow: emitted")
        emit(user)
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmailAndPassword: auth success")
                    user = auth.currentUser
                } else {
                    Log.d(TAG, "signInWithEmailAndPassword: ${task.exception}")
                }
            }
    }

    override suspend fun signOut() {
        auth.signOut()
        Log.d(TAG, "signOut: ${auth.currentUser}")
        user = null
    }

    companion object {
        private const val TAG = "AuthRepositoryImpl"
    }
}