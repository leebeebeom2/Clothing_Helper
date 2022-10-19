package com.leebeebeom.clothinghelper.di.usecase

import com.leebeebeom.clothinghelper.data.repository.SubCategoryRepositoryImpl
import com.leebeebeom.clothinghelper.domain.usecase.subcategory.WriteInitialSubCategoryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SubCategoryUseCaseModule {
    @Singleton
    @Provides
    fun provideWriteSubCategoryUseCase(subCategoryRepository: SubCategoryRepositoryImpl) =
        WriteInitialSubCategoryUseCase(subCategoryRepository)
}