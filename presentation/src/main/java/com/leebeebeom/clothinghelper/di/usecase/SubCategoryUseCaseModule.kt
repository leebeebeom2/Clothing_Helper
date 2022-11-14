package com.leebeebeom.clothinghelper.di.usecase

import com.leebeebeom.clothinghelperdata.repository.SubCategoryRepositoryImpl
import com.leebeebeom.clothinghelperdomain.usecase.preferences.SubCategorySortUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.*
import com.leebeebeom.clothinghelperdomain.usecase.user.GetUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class SubCategoryUseCaseModule {
    @Provides
    fun addSubCategoryUseCase(subCategoryRepository: SubCategoryRepositoryImpl) =
        AddSubCategoryUseCase(subCategoryRepository = subCategoryRepository)

    @Provides
    fun updateSubCategoriesUseCase(
        subCategoryRepository: SubCategoryRepositoryImpl,
        getUserUseCase: GetUserUseCase
    ) = UpdateSubCategoriesUseCase(getUserUseCase, subCategoryRepository)

    @Provides
    fun getAllSubCategoriesUseCase(
        subCategoryRepository: SubCategoryRepositoryImpl,
        subCategorySortUseCase: SubCategorySortUseCase
    ) = GetAllSubCategoriesUseCase(subCategoryRepository, subCategorySortUseCase)

    @Provides
    fun editSubCategoryNameUseCase(subCategoryRepository: SubCategoryRepositoryImpl) =
        EditSubCategoryNameUseCase(subCategoryRepository)

    @Provides
    fun getSubCategoryLoadingStateUseCase(subCategoryRepository: SubCategoryRepositoryImpl) =
        GetSubCategoryLoadingStateUseCase(subCategoryRepository)

    @Provides
    fun pushInitialSubCategoriesUseCase(subCategoryRepository: SubCategoryRepositoryImpl) =
        PushInitialSubCategoriesUseCase(subCategoryRepository)
}