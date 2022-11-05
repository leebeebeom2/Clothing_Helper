package com.leebeebeom.clothinghelper.di.usecase

import com.leebeebeom.clothinghelperdata.repository.SubCategoryPreferencesRepositoryImpl
import com.leebeebeom.clothinghelperdata.repository.SubCategoryRepositoryImpl
import com.leebeebeom.clothinghelperdomain.usecase.signin.GetUserUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.*
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
    fun loadSubCategoriesUseCase(
        subCategoryRepository: SubCategoryRepositoryImpl,
        getUserUseCase: GetUserUseCase
    ) = LoadSubCategoriesUseCase(
        subCategoryRepository = subCategoryRepository,
        getUserUseCase = getUserUseCase
    )

    @Provides
    fun getSubCategoriesUseCase(
        subCategoryRepository: SubCategoryRepositoryImpl,
        subCategoryPreferencesRepository: SubCategoryPreferencesRepositoryImpl
    ) = GetSubCategoriesUseCase(subCategoryRepository, subCategoryPreferencesRepository)

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