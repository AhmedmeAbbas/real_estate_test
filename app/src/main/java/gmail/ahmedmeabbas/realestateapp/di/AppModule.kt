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
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
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
import gmail.ahmedmeabbas.realestateapp.listings.getlisting.data.GetListingRepository
import gmail.ahmedmeabbas.realestateapp.listings.getlisting.data.GetListingRepositoryImpl
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
        return db
    }

    @Singleton
    @Provides
    fun provideFirebaseCloudStorage(): FirebaseStorage {
        val storage = FirebaseStorage.getInstance()
        storage.useEmulator("10.0.2.2", 9199)
        return storage
    }

    @Singleton
    @Provides
    fun provideAddListingRepository(
        db: FirebaseFirestore,
        storage: FirebaseStorage
    ): AddListingRepository {
        return AddListingRepositoryImpl(db, storage)
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
    fun provideGetListingRepository(
        db: FirebaseFirestore
    ): GetListingRepository {
        return GetListingRepositoryImpl(db)
    }
}