package com.leebeebeom.clothinghelper.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.leebeebeom.clothinghelperdata.repository.PreferencesRepositoryImpl
import com.leebeebeom.clothinghelperdata.repository.SubCategoryRepositoryImpl
import com.leebeebeom.clothinghelperdata.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Singleton
    @Provides
    fun subCategoryRepository(
        userRepository: UserRepositoryImpl,
        preferencesRepository: PreferencesRepositoryImpl
    ) = SubCategoryRepositoryImpl(userRepository, preferencesRepository)

    @Singleton
    @Provides
    fun userRepository() = UserRepositoryImpl()

    @Singleton
    @Provides
    fun preferencesRepository(dataStore: DataStore<Preferences>) =
        PreferencesRepositoryImpl(dataStore)

    @Singleton
    @Provides
    fun dataStore(@ApplicationContext context: Context) = context.datastore
}

private const val PREFERENCES_NAME = "preferences"
private val Context.datastore by preferencesDataStore(name = PREFERENCES_NAME)