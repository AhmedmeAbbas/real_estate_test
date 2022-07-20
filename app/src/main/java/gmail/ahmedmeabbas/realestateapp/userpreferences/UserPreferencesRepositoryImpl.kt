package gmail.ahmedmeabbas.realestateapp.userpreferences

import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import gmail.ahmedmeabbas.realestateapp.userpreferences.UserPreferencesRepositoryImpl.PreferencesKeys.APP_LANGUAGE
import gmail.ahmedmeabbas.realestateapp.userpreferences.UserPreferencesRepositoryImpl.PreferencesKeys.NIGHT_MODE
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
        val NIGHT_MODE = intPreferencesKey("night_mode")
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

    override suspend fun fetchNightModeFlag(): Int? {
        val settings = dataStore.data.first()
        return settings[NIGHT_MODE]
    }

    override suspend fun fetchAppLanguage(): String? {
        val settings = dataStore.data.first()
        return settings[APP_LANGUAGE]
    }

    override suspend fun writeNightModeFlag(value: Int) {
        dataStore.edit { settings ->
            settings[NIGHT_MODE] = value
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
        val nightModeFlag = preferences[NIGHT_MODE] ?: AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
        return UserPreferences(language, nightModeFlag)
    }
}