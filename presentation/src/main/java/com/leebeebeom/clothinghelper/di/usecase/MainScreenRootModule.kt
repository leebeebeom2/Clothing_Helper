package com.leebeebeom.clothinghelper.di.usecase

import com.leebeebeom.clothinghelperdata.repository.MainScreenRootPreferencesRepositoryImpl
import com.leebeebeom.clothinghelperdomain.usecase.preferences.MainScreenRootAllExpandUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class MainScreenRootModule {
    @Provides
    fun mainScreenRootAllExpandUseCase(mainScreenRootPreferencesRepository: MainScreenRootPreferencesRepositoryImpl) =
        MainScreenRootAllExpandUseCase(mainScreenRootPreferencesRepository)
}