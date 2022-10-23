package com.leebeebeom.clothinghelper.di.usecase

import com.leebeebeom.clothinghelperdata.repository.PreferencesRepositoryImpl
import com.leebeebeom.clothinghelperdomain.usecase.preferences.GetPreferencesAndToggleAllExpandUseCase
import com.leebeebeom.clothinghelperdomain.usecase.preferences.GetPreferencesUseCase
import com.leebeebeom.clothinghelperdomain.usecase.preferences.ToggleAllExpandUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class PreferencesUseCaseModule {
    @Provides
    fun provideGetPreferencesUseCase(preferencesRepository: PreferencesRepositoryImpl) =
        GetPreferencesUseCase(preferencesRepository)

    @Provides
    fun provideToggleAllExpandUseCase(preferencesRepository: PreferencesRepositoryImpl) =
        ToggleAllExpandUseCase(preferencesRepository)

    @Provides
    fun provideGetPreferencesAndToggleAllExpandUseCase(
        getPreferencesUseCase: GetPreferencesUseCase,
        toggleAllExpandUseCase: ToggleAllExpandUseCase
    ) = GetPreferencesAndToggleAllExpandUseCase(getPreferencesUseCase, toggleAllExpandUseCase)
}