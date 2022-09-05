package gmail.ahmedmeabbas.realestateapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepository
import gmail.ahmedmeabbas.realestateapp.authentication.data.AuthRepositoryImpl
import gmail.ahmedmeabbas.realestateapp.listings.addlisting.data.AddListingRepository
import gmail.ahmedmeabbas.realestateapp.listings.addlisting.data.AddListingRepositoryImpl
import gmail.ahmedmeabbas.realestateapp.listings.addlisting.domain.AddApartmentUseCase
import gmail.ahmedmeabbas.realestateapp.listings.addlisting.domain.AddHouseUseCase
import gmail.ahmedmeabbas.realestateapp.listings.getlisting.data.ListingRepository
import gmail.ahmedmeabbas.realestateapp.listings.getlisting.data.ListingRepositoryImpl
import gmail.ahmedmeabbas.realestateapp.userpreferences.UserPreferencesRepository
import gmail.ahmedmeabbas.realestateapp.userpreferences.UserPreferencesRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

private const val USER_PREFERENCES = "user_preferences"

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun providePreferencesDataStore(
        @ApplicationContext appContext: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(appContext, USER_PREFERENCES)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(USER_PREFERENCES) }
        )
    }

    @Singleton
    @Provides
    fun provideUserPreferencesRepository(
        dataStore: DataStore<Preferences>
    ): UserPreferencesRepository {
        return UserPreferencesRepositoryImpl(dataStore)
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth() = Firebase.auth

    @Singleton
    @Provides
    fun provideAuthRepository(
        auth: FirebaseAuth
    ): AuthRepository {
        return AuthRepositoryImpl(auth)
    }

    @Singleton
    @Provides
    fun provideFirestoreDatabase(): FirebaseFirestore {
        val db = FirebaseFirestore.getInstance()
        db.useEmulator("10.0.2.2", 8080)
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()
        db.firestoreSettings = settings
        return db
    }

    @Singleton
    @Provides
    fun provideAddListingRepository(
        db: FirebaseFirestore
    ): AddListingRepository {
        return AddListingRepositoryImpl(db)
    }

    @Singleton
    @Provides
    fun provideAddApartmentUseCase(
        addListingRepository: AddListingRepository
    ): AddApartmentUseCase {
        return AddApartmentUseCase(addListingRepository)
    }

    @Singleton
    @Provides
    fun provideAddHouseUseCase(
        addListingRepository: AddListingRepository
    ): AddHouseUseCase {
        return AddHouseUseCase(addListingRepository)
    }

    @Singleton
    @Provides
    fun provideListingRepository(
        db: FirebaseFirestore
    ): ListingRepository {
        return ListingRepositoryImpl(db)
    }
}