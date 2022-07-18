package gmail.ahmedmeabbas.realestateapp.userpreferences

interface UserPreferencesDataSource {

    suspend fun writeString(key:String, value: String)

    suspend fun readString(key: String): String?

    suspend fun writeBoolean(key:String, value: Boolean)

    suspend fun readBoolean(key: String): Boolean?
}