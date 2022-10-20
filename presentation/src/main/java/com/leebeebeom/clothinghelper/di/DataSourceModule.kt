package com.leebeebeom.clothinghelper.di

import com.leebeebeom.clothinghelperdata.datasource.SubCategoryRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Singleton
    @Provides
    fun provideSubCategoryRemoteDataSource() = SubCategoryRemoteDataSource()
}