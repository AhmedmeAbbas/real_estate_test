package gmail.ahmedmeabbas.realestateapp.util

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gmail.ahmedmeabbas.realestateapp.userpreferences.UserPreferencesRepository

@EntryPoint
@InstallIn(SingletonComponent::class)
interface UserPrefsEntryPoint {
    val userPrefsRepo: UserPreferencesRepository
}