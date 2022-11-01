package com.leebeebeom.clothinghelper.di.usecase

import com.leebeebeom.clothinghelperdata.repository.SubCategoryPreferencesRepositoryImpl
import com.leebeebeom.clothinghelperdomain.usecase.preferences.SubCategoryAllExpandUseCase
import com.leebeebeom.clothinghelperdomain.usecase.preferences.SubCategorySortUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class SubCategoryPreferencesUseCaseModule {
    @Provides
    fun getSubCategoryUseCase(subCategoryPreferencesRepository: SubCategoryPreferencesRepositoryImpl) =
        SubCategorySortUseCase(subCategoryPreferencesRepository)

    @Provides
    fun subCategoryAllExpandUseCase(subCategoryPreferencesRepository: SubCategoryPreferencesRepositoryImpl) =
        SubCategoryAllExpandUseCase(subCategoryPreferencesRepository)
}