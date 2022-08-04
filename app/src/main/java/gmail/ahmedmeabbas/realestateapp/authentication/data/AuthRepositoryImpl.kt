package gmail.ahmedmeabbas.realestateapp.authentication.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    private val _isUserSignedInFlow: MutableStateFlow<Boolean> = MutableStateFlow(auth.currentUser != null)
    override val isUserSignedInFlow: StateFlow<Boolean> = _isUserSignedInFlow.asStateFlow()
    private var user: FirebaseUser? = auth.currentUser
    override val userFlow: Flow<FirebaseUser?> = flowOf(user)

    init {
        observeAuthStateChange()
    }

    override suspend fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String): Boolean {
        var isSignInSuccessful = false
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmailAndPassword: auth success")
                    user = auth.currentUser
                    isSignInSuccessful = true
                } else {
                    Log.d(TAG, "signInWithEmailAndPassword: ${task.exception}")
                    isSignInSuccessful = false
                }
            }
        return isSignInSuccessful
    }

    override suspend fun signOut() {
        auth.signOut()
        Log.d(TAG, "signOut: ${auth.currentUser}")
        user = null
    }

    private fun observeAuthStateChange() {
        auth.addAuthStateListener { firebaseAuth ->
            Log.d(TAG, "observeAuthStateChange: auth state changed")
            _isUserSignedInFlow.value = firebaseAuth.currentUser != null
            Log.d(TAG, "observeAuthStateChange: signed in: ${_isUserSignedInFlow.value}")
        }
    }

    companion object {
        private const val TAG = "AuthRepositoryImpl"
    }
}