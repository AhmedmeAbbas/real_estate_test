package gmail.ahmedmeabbas.realestateapp.authentication.data

interface AuthRepository {

    suspend fun signInWithEmailAndPassword(email: String, password: String)
}