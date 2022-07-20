package gmail.ahmedmeabbas.realestateapp.userpreferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import gmail.ahmedmeabbas.realestateapp.userpreferences.UserPreferencesRepositoryImpl.PreferencesKeys.APP_LANGUAGE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.*
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {

    private object PreferencesKeys {
        val APP_LANGUAGE = stringPreferencesKey("app_language")
    }

    override val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    override suspend fun readBoolean(key: String): Boolean? {
        val dataStoreKey = booleanPreferencesKey(key)
        val settings = dataStore.data.first()
        return settings[dataStoreKey]
    }

    override suspend fun fetchAppLanguage(): String? {
        val settings = dataStore.data.first()
        return settings[APP_LANGUAGE]
    }

    override suspend fun writeBoolean(key: String, value: Boolean) {
        val dataStoreKey = booleanPreferencesKey(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    override suspend fun writeAppLanguage(value: String) {
        dataStore.edit { settings ->
            settings[APP_LANGUAGE] = value
        }
    }

    override suspend fun fetchInitialPreferences(): UserPreferences {
        return mapUserPreferences(dataStore.data.first().toPreferences())
    }

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val language = preferences[APP_LANGUAGE] ?: Locale.getDefault().language
        return UserPreferences(language)
    }
}