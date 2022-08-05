package gmail.ahmedmeabbas.realestateapp.authentication.util

sealed class AuthResource<T>(val data: T? = null, val message: ErrorMessage? = null) {
    class Success<T>(data: T?): AuthResource<T>(data)
    class Error<T>(message: ErrorMessage?, data: T?): AuthResource<T>(data, message)
}

data class ErrorMessage(
    val type: ErrorMessageType,
    val message: String
)

enum class ErrorMessageType {
    EMAIL_SIGN_IN,
    FACEBOOK_SIGN_IN,
    GOOGLE_SIGN_IN,
    CREATE_ACCOUNT
}
