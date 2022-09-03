package gmail.ahmedmeabbas.realestateapp.authentication.util

data class AuthMessage(
    val type: AuthMessageType,
    val message: String
)

enum class AuthMessageType {
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
