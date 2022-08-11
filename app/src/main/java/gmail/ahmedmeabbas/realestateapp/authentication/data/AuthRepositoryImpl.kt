package gmail.ahmedmeabbas.realestateapp.authentication.data

import com.facebook.AccessToken
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
                    val user = auth.currentUser
                    if (user?.displayName == null) {
                        addDefaultDisplayName()
                        return@addOnCompleteListener
                    }
                    _userFlow.value = user
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
                    _userFlow.value = auth.currentUser
                    scope.launch {
                        _authMessagesFlow.emit(
                            AuthMessage(
                                AuthMessageType.DISPLAY_NAME,
                                SUCCESS
                            )
                        )
                    }
                } else {
                    scope.launch {
                        _authMessagesFlow.emit(
                            AuthMessage(
                                AuthMessageType.DISPLAY_NAME,
                                FAILURE
                            )
                        )
                    }
                }
            }
    }

    override suspend fun reAuthenticateUser(email: String, password: String) {
        val user = auth.currentUser
        val credential = EmailAuthProvider.getCredential(email, password)
        user?.reauthenticate(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    scope.launch {
                        _authMessagesFlow.emit(
                            AuthMessage(
                                AuthMessageType.RE_AUTHENTICATE,
                                RE_AUTHENTICATE_SUCCESS
                            )
                        )
                    }
                } else {
                    scope.launch {
                        _authMessagesFlow.emit(
                            AuthMessage(
                                AuthMessageType.RE_AUTHENTICATE,
                                RE_AUTHENTICATE_FAILURE
                            )
                        )
                    }
                }
            }
    }

    override suspend fun updateEmail(email: String) {
        val user = auth.currentUser
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

    override suspend fun updatePassword(password: String) {
        val user = auth.currentUser
        user?.updatePassword(password)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    scope.launch {
                        _authMessagesFlow.emit(
                            AuthMessage(
                                AuthMessageType.EDIT_PASSWORD,
                                EDIT_PASSWORD_SUCCESS
                            )
                        )
                    }
                } else {
                    scope.launch {
                        _authMessagesFlow.emit(
                            AuthMessage(
                                AuthMessageType.EDIT_PASSWORD,
                                EDIT_PASSWORD_FAILURE
                            )
                        )
                    }

                }
            }
    }

    override suspend fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    scope.launch {
                        _authMessagesFlow.emit(
                            AuthMessage(
                                AuthMessageType.RESET_PASSWORD,
                                SUCCESS
                            )
                        )
                    }
                } else {
                    scope.launch {
                        _authMessagesFlow.emit(
                            AuthMessage(
                                AuthMessageType.RESET_PASSWORD,
                                FAILURE
                            )
                        )
                    }

                }
            }
    }

    override suspend fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    addDefaultDisplayName()
                    scope.launch {
                        _authMessagesFlow.emit(
                            AuthMessage(
                                AuthMessageType.CREATE_ACCOUNT,
                                SUCCESS
                            )
                        )
                    }
                } else {
                    scope.launch {
                        _authMessagesFlow.emit(
                            AuthMessage(
                                AuthMessageType.CREATE_ACCOUNT,
                                FAILURE
                            )
                        )
                    }
                }
            }
    }

    private fun addDefaultDisplayName() {
        val user = auth.currentUser
        val defaultName = user?.email?.substringBefore("@") ?: ""
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(defaultName)
            .build()
        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener {
                _userFlow.value = user
            }
    }

    override suspend fun handleFacebookAccessToken(accessToken: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _userFlow.value = auth.currentUser
                    scope.launch {
                        _authMessagesFlow.emit(
                            AuthMessage(
                                AuthMessageType.FACEBOOK_SIGN_IN,
                                SUCCESS
                            )
                        )
                    }
                } else {
                    scope.launch {
                        _authMessagesFlow.emit(
                            AuthMessage(
                                AuthMessageType.FACEBOOK_SIGN_IN,
                                FAILURE
                            )
                        )
                    }
                }
            }
    }

    companion object {
        const val SUCCESS = "success"
        const val FAILURE = "failure"
        const val INVALID_CREDENTIALS = "invalid_credentials"
        const val RE_AUTHENTICATE_SUCCESS = "re_auth_success"
        const val RE_AUTHENTICATE_FAILURE = "re_auth_failure"
        const val EDIT_EMAIL_SUCCESS = "edit_email_success"
        const val EDIT_EMAIL_FAILURE = "edit_email_failure"
        const val EDIT_PASSWORD_SUCCESS = "edit_password_success"
        const val EDIT_PASSWORD_FAILURE = "edit_password_failure"
        private const val TAG = "AuthRepositoryImpl"
    }
}