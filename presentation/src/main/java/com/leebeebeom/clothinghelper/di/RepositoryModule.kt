package com.leebeebeom.clothinghelper.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.leebeebeom.clothinghelperdata.repository.SubCategoryPreferencesRepositoryImpl
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
        subCategoryPreferencesRepository: SubCategoryPreferencesRepositoryImpl
    ) = SubCategoryRepositoryImpl(userRepository, subCategoryPreferencesRepository)

    @Singleton
    @Provides
    fun userRepository() = UserRepositoryImpl()

    @Singleton
    @Provides
    fun preferencesRepository(dataStore: DataStore<Preferences>) =
        SubCategoryPreferencesRepositoryImpl(dataStore)

    @Singleton
    @Provides
    fun dataStore(@ApplicationContext context: Context) = context.subCategoryDatastore
}

private const val SUBCATEGORY = "subCategory_preferences"
private val Context.subCategoryDatastore by preferencesDataStore(name = SUBCATEGORY)