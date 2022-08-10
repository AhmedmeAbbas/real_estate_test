package gmail.ahmedmeabbas.realestateapp.authentication.util

sealed class AuthResource<T>(val data: T? = null, val message: AuthMessage? = null) {
    class Success<T>(data: T?): AuthResource<T>(data)
    class Error<T>(message: AuthMessage?, data: T?): AuthResource<T>(data, message)
}

data class AuthMessage(
    val type: AuthMessageType,
    val message: String
)

enum class AuthMessageType {
    ERROR_OCCURRED,
    EMAIL_SIGN_IN,
    FACEBOOK_SIGN_IN,
    GOOGLE_SIGN_IN,
    CREATE_ACCOUNT,
    RE_AUTHENTICATE,
    DISPLAY_NAME,
    EDIT_EMAIL,
    EDIT_PASSWORD,
    RESET_PASSWORD
}
