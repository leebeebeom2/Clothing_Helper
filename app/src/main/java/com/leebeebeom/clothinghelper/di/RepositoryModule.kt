package com.leebeebeom.clothinghelper.di

import com.leebeebeom.clothinghelper.data.repository.TodoRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.UserRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.container.FolderRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.container.SubCategoryRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.preference.FolderPreferencesRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.preference.NetworkPreferenceRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.preference.SubCategoryPreferencesRepositoryImpl
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.FolderPreferencesRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.NetworkPreferenceRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SubCategoryPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun userRepository(impl: UserRepositoryImpl): UserRepository

    @Singleton
    @Binds
    abstract fun subCategoryRepository(impl: SubCategoryRepositoryImpl): SubCategoryRepository

    @Singleton
    @Binds
    abstract fun folderRepository(impl: FolderRepositoryImpl): FolderRepository

    @Singleton
    @Binds
    abstract fun todoRepository(impl: TodoRepositoryImpl): TodoRepository

    @Singleton
    @Binds
    abstract fun networkPreferenceRepository(impl: NetworkPreferenceRepositoryImpl): NetworkPreferenceRepository

    @Singleton
    @Binds
    @SubCategoryPreferencesRepository
    abstract fun subCategoryPreferencesRepository(impl: SubCategoryPreferencesRepositoryImpl): SortPreferenceRepository

    @Singleton
    @Binds
    @FolderPreferencesRepository
    abstract fun folderPreferencesRepository(impl: FolderPreferencesRepositoryImpl): SortPreferenceRepository
}