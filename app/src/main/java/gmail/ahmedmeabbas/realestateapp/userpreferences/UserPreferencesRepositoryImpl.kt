package gmail.ahmedmeabbas.realestateapp.userpreferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.EntryPoint
import gmail.ahmedmeabbas.realestateapp.userpreferences.UserPreferencesRepositoryImpl.PreferencesKeys.APP_LANGUAGE
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {

    private object PreferencesKeys {
        val APP_LANGUAGE = stringPreferencesKey("app_language")
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
}