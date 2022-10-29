package com.leebeebeom.clothinghelper.di.usecase

import com.leebeebeom.clothinghelperdata.repository.SubCategoryPreferencesRepositoryImpl
import com.leebeebeom.clothinghelperdomain.usecase.preferences.GetSubCategoryPreferencesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.preferences.ToggleAllExpandUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class SubCategoryPreferencesUseCaseModule {
    @Provides
    fun getSubCategoryPreferencesUseCase(subCategoryPreferencesRepository: SubCategoryPreferencesRepositoryImpl) =
        GetSubCategoryPreferencesUseCase(subCategoryPreferencesRepository)

    @Provides
    fun toggleAllExpandUseCase(subCategoryPreferencesRepository: SubCategoryPreferencesRepositoryImpl) =
        ToggleAllExpandUseCase(subCategoryPreferencesRepository)
}