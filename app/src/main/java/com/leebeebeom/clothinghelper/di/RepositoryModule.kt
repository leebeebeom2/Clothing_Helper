package com.leebeebeom.clothinghelper.di

import com.leebeebeom.clothinghelper.data.repository.FolderRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.TodoRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.UserRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.preference.FolderPreferencesRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.preference.MainScreenPreferencesRepositoryImpl
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.FolderPreferenceRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.MainScreenPreferencesRepository
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
    abstract fun folderRepository(impl: FolderRepositoryImpl): FolderRepository

    @Singleton
    @Binds
    abstract fun todoRepository(impl: TodoRepositoryImpl): TodoRepository

    @Singleton
    @Binds
    abstract fun folderPreferencesRepository(impl: FolderPreferencesRepositoryImpl): FolderPreferenceRepository

    @Singleton
    @Binds
    abstract fun mainScreenPreferencesRepository(impl: MainScreenPreferencesRepositoryImpl): MainScreenPreferencesRepository
}