package gmail.ahmedmeabbas.realestateapp.authentication.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ErrorMessageType {
    EMAIL_SIGN_IN,
    FACEBOOK_SIGN_IN,
    GOOGLE_SIGN_IN,
    CREATE_ACCOUNT
}

data class ErrorMessage(
    val type: ErrorMessageType,
    val message: String
)

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    private val _isUserSignedInFlow = MutableStateFlow(auth.currentUser != null)
    override val isUserSignedInFlow: StateFlow<Boolean> = _isUserSignedInFlow.asStateFlow()
    private val _errorMessagesFlow: MutableSharedFlow<ErrorMessage> = MutableSharedFlow()
    override val errorMessageFlow: SharedFlow<ErrorMessage> = _errorMessagesFlow.asSharedFlow()
    private var user: FirebaseUser? = auth.currentUser
    override val userFlow: Flow<FirebaseUser?> = flowOf(user)
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        observeAuthStateChange()
    }

    override suspend fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmailAndPassword: auth success")
                    user = auth.currentUser
                } else {
                    Log.d(TAG, "signInWithEmailAndPassword: ${task.exception}")
                    val e = task.exception
                    if (e is FirebaseAuthInvalidUserException ||
                        e is FirebaseAuthInvalidCredentialsException
                    ) {
                        Log.d(TAG, "signInWithEmailAndPassword: if triggered")
                        scope.launch {
                            _errorMessagesFlow.emit(ErrorMessage(
                                ErrorMessageType.EMAIL_SIGN_IN,
                                "Invalid email or password"
                            ))
                        }
                    } else {
                        Log.d(TAG, "signInWithEmailAndPassword: else triggered")
                        scope.launch {
                            _errorMessagesFlow.emit(ErrorMessage(
                                ErrorMessageType.EMAIL_SIGN_IN,
                                task.exception?.message.toString()
                            ))
                        }
                    }
                }
            }
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