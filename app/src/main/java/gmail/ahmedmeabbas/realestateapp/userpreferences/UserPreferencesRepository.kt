package gmail.ahmedmeabbas.realestateapp.userpreferences

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {

    val userPreferencesFlow: Flow<UserPreferences>

    suspend fun fetchInitialPreferences(): UserPreferences

    suspend fun writeAppLanguage(value: String)

    suspend fun fetchAppLanguage(): String?

    suspend fun writeBoolean(key:String, value: Boolean)

    suspend fun readBoolean(key: String): Boolean?
}