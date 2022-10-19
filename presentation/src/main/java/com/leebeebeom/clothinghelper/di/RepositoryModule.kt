package com.leebeebeom.clothinghelper.di

import com.leebeebeom.clothinghelper.data.datasource.SubCategoryRemoteDataSource
import com.leebeebeom.clothinghelper.data.repository.SubCategoryRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.UserRepositoryImpl
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
    fun provideSubCategoryRepository(subCategoryRemoteDataSource: SubCategoryRemoteDataSource) =
        SubCategoryRepositoryImpl(subCategoryRemoteDataSource)

    @Singleton
    @Provides
    fun provideUserRepository() = UserRepositoryImpl()
}