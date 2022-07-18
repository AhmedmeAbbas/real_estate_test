package gmail.ahmedmeabbas.realestateapp.userpreferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

class UserPreferencesRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {

    override suspend fun readBoolean(key: String): Boolean? {
        val dataStoreKey = booleanPreferencesKey(key)
        val settings = dataStore.data.first()
        return settings[dataStoreKey]
    }

    override suspend fun readString(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val settings = dataStore.data.first()
        return settings[dataStoreKey]
    }

    override suspend fun writeBoolean(key: String, value: Boolean) {
        val dataStoreKey = booleanPreferencesKey(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    override suspend fun writeString(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }
}