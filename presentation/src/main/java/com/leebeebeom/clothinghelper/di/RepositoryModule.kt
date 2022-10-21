package com.leebeebeom.clothinghelper.di

import com.leebeebeom.clothinghelperdata.repository.SubCategoryRepositoryImpl
import com.leebeebeom.clothinghelperdata.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Singleton
    @Provides
    fun provideSubCategoryRepository(userRepository: UserRepositoryImpl) =
        SubCategoryRepositoryImpl(userRepository)

    @Singleton
    @Provides
    fun provideUserRepository() = UserRepositoryImpl()
}