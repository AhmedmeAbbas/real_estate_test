package gmail.ahmedmeabbas.realestateapp.userpreferences

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {

    val userPreferencesFlow: Flow<UserPreferences>

    suspend fun fetchInitialPreferences(): UserPreferences

    suspend fun writeAppLanguage(value: String)

    suspend fun fetchAppLanguage(): String?

    suspend fun writeNightModeBoolean(value: Boolean)

    suspend fun fetchNightModeBoolean(): Boolean?
}