package gmail.ahmedmeabbas.realestateapp.userpreferences

interface UserPreferencesRepository {

    suspend fun writeAppLanguage(value: String)

    suspend fun fetchAppLanguage(): String?

    suspend fun writeBoolean(key:String, value: Boolean)

    suspend fun readBoolean(key: String): Boolean?
}