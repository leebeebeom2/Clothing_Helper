package com.leebeebeom.clothinghelper.di.usecase

import com.leebeebeom.clothinghelperdata.repository.SubCategoryRepositoryImpl
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class SubCategoryUseCaseModule {
    @Provides
    fun provideWriteSubCategoryUseCase(subCategoryRepository: SubCategoryRepositoryImpl) =
        WriteInitialSubCategoriesUseCase(subCategoryRepository)

    @Provides
    fun provideAddSubCategoryUseCase(subCategoryRepository: SubCategoryRepositoryImpl) =
        AddSubCategoryUseCase(subCategoryRepository)

    @Provides
    fun provideGetSubCategoriesUseCase(
        getTopSubcategoriesUseCase: GetTopSubcategoriesUseCase,
        getBottomSubcategoriesUseCase: GetBottomSubcategoriesUseCase,
        getOuterSubcategoriesUseCase: GetOuterSubcategoriesUseCase,
        getEtcSubcategoriesUseCase: GetEtcSubcategoriesUseCase
    ) = GetSubCategoriesUserCase(
        getTopSubcategoriesUseCase,
        getBottomSubcategoriesUseCase,
        getOuterSubcategoriesUseCase,
        getEtcSubcategoriesUseCase
    )

    @Provides
    fun provideTopGetSubCategoriesUseCase(subCategoryRepository: SubCategoryRepositoryImpl) =
        GetTopSubcategoriesUseCase(subCategoryRepository)

    @Provides
    fun provideBottomGetSubCategoriesUseCase(subCategoryRepository: SubCategoryRepositoryImpl) =
        GetBottomSubcategoriesUseCase(subCategoryRepository)

    @Provides
    fun provideOuterGetSubCategoriesUseCase(subCategoryRepository: SubCategoryRepositoryImpl) =
        GetOuterSubcategoriesUseCase(subCategoryRepository)

    @Provides
    fun provideEtcGetSubCategoriesUseCase(subCategoryRepository: SubCategoryRepositoryImpl) =
        GetEtcSubcategoriesUseCase(subCategoryRepository)
}