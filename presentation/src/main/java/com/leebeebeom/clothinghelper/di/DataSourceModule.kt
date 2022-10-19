package com.leebeebeom.clothinghelper.di

import com.leebeebeom.clothinghelper.data.datasource.SubCategoryRemoteDataSource
import com.leebeebeom.clothinghelper.domain.usecase.user.UserInfoUserCase
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
    fun provideSubCategoryRemoteDataSource(userInfoUserCase: UserInfoUserCase) =
        SubCategoryRemoteDataSource(userInfoUserCase)
}