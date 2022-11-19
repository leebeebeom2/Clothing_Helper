package com.leebeebeom.clothinghelper.di

import com.leebeebeom.clothinghelperdata.repository.UserRepositoryImpl
import com.leebeebeom.clothinghelperdata.repository.container.SubCategoryRepositoryImpl
import com.leebeebeom.clothinghelperdata.repository.preferences.FolderPreferencesRepositoryImpl
import com.leebeebeom.clothinghelperdata.repository.preferences.SubCategoryPreferencesRepositoryImpl
import com.leebeebeom.clothinghelperdomain.repository.*
import com.leebeebeom.clothinghelperdomain.repository.preferences.FolderPreferencesRepository
import com.leebeebeom.clothinghelperdomain.repository.preferences.SubCategoryPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun userRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun subCategoryRepository(impl: SubCategoryRepositoryImpl): SubCategoryRepository

    @Binds
    abstract fun subCategoryPreferencesRepository(impl: SubCategoryPreferencesRepositoryImpl): SubCategoryPreferencesRepository

    @Binds
    abstract fun folderPreferencesRepository(impl: FolderPreferencesRepositoryImpl): FolderPreferencesRepository
}