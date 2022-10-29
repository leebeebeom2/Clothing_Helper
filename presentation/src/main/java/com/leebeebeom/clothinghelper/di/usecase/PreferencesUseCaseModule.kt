package com.leebeebeom.clothinghelper.di.usecase

import com.leebeebeom.clothinghelperdata.repository.PreferencesRepositoryImpl
import com.leebeebeom.clothinghelperdomain.usecase.preferences.GetSubCategoryPreferencesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.preferences.ToggleAllExpandUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class PreferencesUseCaseModule {
    @Provides
    fun getPreferencesUseCase(preferencesRepository: PreferencesRepositoryImpl) =
        GetSubCategoryPreferencesUseCase(preferencesRepository)

    @Provides
    fun toggleAllExpandUseCase(preferencesRepository: PreferencesRepositoryImpl) =
        ToggleAllExpandUseCase(preferencesRepository)
}