package com.leebeebeom.clothinghelper.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.leebeebeom.clothinghelperdata.repository.preferences.MainRootPreferencesRepositoryImpl
import com.leebeebeom.clothinghelperdata.repository.preferences.SubCategoryPreferencesRepositoryImpl
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
    fun subCategoryRepository() = SubCategoryRepositoryImpl()

    @Singleton
    @Provides
    fun userRepository() = UserRepositoryImpl()

    @Singleton
    @Provides
    fun subCategoryPreferencesRepository(@ApplicationContext context: Context) =
        SubCategoryPreferencesRepositoryImpl(context.subCategoryDatastore)

    @Singleton
    @Provides
    fun mainScreenRootPreferencesRepository(@ApplicationContext context: Context) =
        MainRootPreferencesRepositoryImpl(context.mainScreenRootDatastore)
}

private const val SUBCATEGORY = "subCategory_preferences"
private const val MAIN_SCREEN_ROO = "main_screen_root_preferences"
private val Context.subCategoryDatastore by preferencesDataStore(name = SUBCATEGORY)
private val Context.mainScreenRootDatastore by preferencesDataStore(name = MAIN_SCREEN_ROO)