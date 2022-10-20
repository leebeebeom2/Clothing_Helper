package com.leebeebeom.clothinghelper.di.usecase

import com.leebeebeom.clothinghelperdata.repository.SubCategoryRepositoryImpl
import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.AddSubCategoryUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetSubCategoriesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.WriteInitialSubCategoryUseCase
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
    fun provideWriteSubCategoryUseCase(
        subCategoryRepository: SubCategoryRepositoryImpl,
        userRepository: UserRepository
    ) = WriteInitialSubCategoryUseCase(subCategoryRepository, userRepository)

    @Singleton
    @Provides
    fun provideAddSubCategoryUseCase(subCategoryRepository: SubCategoryRepositoryImpl) =
        AddSubCategoryUseCase(subCategoryRepository)

    @Singleton
    @Provides
    fun provideGetSubCategoriesUseCase(subCategoryRepository: SubCategoryRepositoryImpl) =
        GetSubCategoriesUseCase(subCategoryRepository)
}