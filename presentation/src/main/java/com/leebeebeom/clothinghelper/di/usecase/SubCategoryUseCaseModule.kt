package com.leebeebeom.clothinghelper.di.usecase

import com.leebeebeom.clothinghelperdata.repository.SubCategoryRepositoryImpl
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.AddSubCategoryUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.GetSubCategoriesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.WriteInitialSubCategoryUseCase
import com.leebeebeom.clothinghelperdomain.usecase.user.UserInfoUserCase
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
        userInfoUserCase: UserInfoUserCase
    ) = WriteInitialSubCategoryUseCase(subCategoryRepository, userInfoUserCase)

    @Singleton
    @Provides
    fun provideAddSubCategoryUseCase(subCategoryRepository: SubCategoryRepositoryImpl) =
        AddSubCategoryUseCase(subCategoryRepository)

    @Singleton
    @Provides
    fun provideGetSubCategoriesUseCase(subCategoryRepository: SubCategoryRepositoryImpl) =
        GetSubCategoriesUseCase(subCategoryRepository)
}