package com.leebeebeom.clothinghelper.di

import com.leebeebeom.clothinghelperdata.repository.UserRepositoryImpl
import com.leebeebeom.clothinghelperdata.repository.container.FolderRepositoryImpl
import com.leebeebeom.clothinghelperdata.repository.container.SubCategoryRepositoryImpl
import com.leebeebeom.clothinghelperdata.repository.preference.FolderPreferencesRepositoryImpl
import com.leebeebeom.clothinghelperdata.repository.preference.SubCategoryPreferencesRepositoryImpl
import com.leebeebeom.clothinghelperdomain.repository.*
import com.leebeebeom.clothinghelperdomain.repository.preference.FolderPreferencesRepository
import com.leebeebeom.clothinghelperdomain.repository.preference.SortPreferenceRepository
import com.leebeebeom.clothinghelperdomain.repository.preference.SubCategoryPreferencesRepository
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
    abstract fun folderRepository(impl: FolderRepositoryImpl): FolderRepository

    @Binds
    @SubCategoryPreferencesRepository
    abstract fun subCategoryPreferencesRepository(impl: SubCategoryPreferencesRepositoryImpl): SortPreferenceRepository

    @Binds
    @FolderPreferencesRepository
    abstract fun folderPreferencesRepository(impl: FolderPreferencesRepositoryImpl): SortPreferenceRepository
}