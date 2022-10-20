package com.leebeebeom.clothinghelper.di.usecase

import com.leebeebeom.clothinghelperdata.repository.SubCategoryRepositoryImpl
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.AddSubCategoryUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.WriteInitialSubCategoriesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.user.GetUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class SubCategoryUseCaseModule {
    @Singleton
    @Provides
    fun provideWriteSubCategoryUseCase(subCategoryRepository: SubCategoryRepositoryImpl) =
        WriteInitialSubCategoriesUseCase(subCategoryRepository)

    @Singleton
    @Provides
    fun provideAddSubCategoryUseCase(
        subCategoryRepository: SubCategoryRepositoryImpl,
        getUserUseCase: GetUserUseCase
    ) = AddSubCategoryUseCase(subCategoryRepository, getUserUseCase)
}