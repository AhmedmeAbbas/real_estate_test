package gmail.ahmedmeabbas.realestateapp.authentication.data

import android.util.Log
import com.google.firebase.auth.*
import gmail.ahmedmeabbas.realestateapp.authentication.util.ErrorMessage
import gmail.ahmedmeabbas.realestateapp.authentication.util.ErrorMessageType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    private val _isUserSignedInFlow = MutableStateFlow(auth.currentUser != null)
    override val isUserSignedInFlow: StateFlow<Boolean> = _isUserSignedInFlow.asStateFlow()
    private val _errorMessagesFlow = MutableSharedFlow<ErrorMessage>()
    override val errorMessageFlow: SharedFlow<ErrorMessage> = _errorMessagesFlow.asSharedFlow()
    private val _userFlow = MutableSharedFlow<FirebaseUser?>()
    override val userFlow: SharedFlow<FirebaseUser?> = _userFlow.asSharedFlow()
    private val _displayNameFlow = MutableSharedFlow<String?>()
    override val displayNameFlow: SharedFlow<String?> = _displayNameFlow.asSharedFlow()
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        observeAuthStateChange()
    }

    private fun observeAuthStateChange() {
        auth.addAuthStateListener { firebaseAuth ->
            _isUserSignedInFlow.value = firebaseAuth.currentUser != null
        }
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val _displayName = user?.displayName
                    val nameFromEmail = user?.email?.substringBefore("@")
                    val displayName =
                        if (_displayName.isNullOrEmpty()) nameFromEmail else _displayName
                    scope.launch {
                        _userFlow.emit(user)
                    }
                    scope.launch {
                        _displayNameFlow.emit(displayName)
                    }
                } else {
                    val e = task.exception
                    if (e is FirebaseAuthInvalidUserException ||
                        e is FirebaseAuthInvalidCredentialsException
                    ) {
                        scope.launch {
                            _errorMessagesFlow.emit(
                                ErrorMessage(
                                    ErrorMessageType.EMAIL_SIGN_IN,
                                    INVALID_CREDENTIALS
                                )
                            )
                        }
                    } else {
                        scope.launch {
                            _errorMessagesFlow.emit(
                                ErrorMessage(
                                    ErrorMessageType.EMAIL_SIGN_IN,
                                    task.exception?.message.toString()
                                )
                            )
                        }
                    }
                }
            }
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun updateDisplayName(displayName: String) {
        val user = auth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(displayName)
            .build()
        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    scope.launch {
                        _displayNameFlow.emit(user.displayName)
                    }
                } else {
                    scope.launch {
                        _errorMessagesFlow.emit(
                            ErrorMessage(
                                ErrorMessageType.DISPLAY_NAME,
                                DISPLAY_NAME_ERROR
                            )
                        )
                    }
                }
            }
    }

    companion object {
        const val INVALID_CREDENTIALS = "invalid_credentials"
        const val DISPLAY_NAME_ERROR = "display_name_error"
        private const val TAG = "AuthRepositoryImpl"
    }
}