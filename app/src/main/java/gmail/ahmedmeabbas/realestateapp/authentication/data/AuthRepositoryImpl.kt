package gmail.ahmedmeabbas.realestateapp.authentication.data

import android.util.Log
import com.google.firebase.auth.*
import gmail.ahmedmeabbas.realestateapp.authentication.util.AuthMessage
import gmail.ahmedmeabbas.realestateapp.authentication.util.AuthMessageType
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
    private val _authMessagesFlow = MutableSharedFlow<AuthMessage>()
    override val authMessagesFlow: SharedFlow<AuthMessage> = _authMessagesFlow.asSharedFlow()
    private val _userFlow = MutableStateFlow(auth.currentUser)
    override val userFlow = _userFlow.asStateFlow()
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
                    _userFlow.value = auth.currentUser
                } else {
                    val e = task.exception
                    if (e is FirebaseAuthInvalidUserException ||
                        e is FirebaseAuthInvalidCredentialsException
                    ) {
                        scope.launch {
                            _authMessagesFlow.emit(
                                AuthMessage(
                                    AuthMessageType.EMAIL_SIGN_IN,
                                    INVALID_CREDENTIALS
                                )
                            )
                        }
                    } else {
                        scope.launch {
                            _authMessagesFlow.emit(
                                AuthMessage(
                                    AuthMessageType.EMAIL_SIGN_IN,
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
                        _authMessagesFlow.emit(
                            AuthMessage(
                                AuthMessageType.DISPLAY_NAME,
                                DISPLAY_NAME_SUCCESS
                            )
                        )
                    }
                } else {
                    scope.launch {
                        _authMessagesFlow.emit(
                            AuthMessage(
                                AuthMessageType.DISPLAY_NAME,
                                DISPLAY_NAME_ERROR
                            )
                        )
                    }
                }
            }
    }

    override suspend fun updateEmail(email: String) {
        val user = auth.currentUser
        Log.d(TAG, "auth repo: user: $user")
        user?.updateEmail(email)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    scope.launch {
                        _authMessagesFlow.emit(
                            AuthMessage(
                                AuthMessageType.EDIT_EMAIL,
                                EDIT_EMAIL_SUCCESS
                            )
                        )
                    }
                } else {
                    Log.d(TAG, "auth repo: exception: ${task.exception}")
                    when (task.exception) {
                        is FirebaseAuthRecentLoginRequiredException -> {
                            scope.launch {
                                _authMessagesFlow.emit(
                                    AuthMessage(
                                        AuthMessageType.EDIT_EMAIL,
                                        LOGIN_REQUIRED
                                    )
                                )
                            }
                        }
                        else -> {
                            scope.launch {
                                _authMessagesFlow.emit(
                                    AuthMessage(
                                        AuthMessageType.EDIT_EMAIL,
                                        EDIT_EMAIL_FAILURE
                                    )
                                )
                            }
                        }
                    }
                }
            }
    }

    companion object {
        const val INVALID_CREDENTIALS = "invalid_credentials"
        const val DISPLAY_NAME_SUCCESS = "display_name_success"
        const val DISPLAY_NAME_ERROR = "display_name_error"
        const val EDIT_EMAIL_SUCCESS = "edit_email_success"
        const val EDIT_EMAIL_FAILURE = "edit_email_failure"
        const val LOGIN_REQUIRED = "login_required"
        private const val TAG = "AuthRepositoryImpl"
    }
}