package com.leebeebeom.clothinghelper.di

import com.leebeebeom.clothinghelper.data.repository.UserRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.container.FolderRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.container.SubCategoryRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.preference.FolderPreferencesRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.preference.SubCategoryPreferencesRepositoryImpl
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.FolderPreferencesRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SubCategoryPreferencesRepository
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