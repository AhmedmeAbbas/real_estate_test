package gmail.ahmedmeabbas.realestateapp.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class DataStoreManager(private val context: Context) {

    suspend fun readBoolean(key: String): Boolean? {
        val dataStoreKey = booleanPreferencesKey(key)
        val settings = context.dataStore.data.first()
        return settings[dataStoreKey]
    }

    suspend fun readString(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val settings = context.dataStore.data.first()
        return settings[dataStoreKey]
    }

    suspend fun writeBoolean(key: String, value: Boolean) {
        val dataStoreKey = booleanPreferencesKey(key)
        context.dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    suspend fun writeString(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context.dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    companion object {
        const val APP_LANGUAGE = "appLanguage"
    }
}