package com.leebeebeom.clothinghelper.domain.usecase.mainscreen

import com.leebeebeom.clothinghelper.domain.repository.preference.MainScreenPreferencesRepository
import com.leebeebeom.clothinghelper.ui.drawer.content.mainmenu.MainMenuType
import javax.inject.Inject

class MainScreenMainMenuExpandToggleUseCase @Inject constructor(
    private val mainScreenPreferencesRepository: MainScreenPreferencesRepository
) {
    suspend fun expandToggle(mainMenuType: MainMenuType) =
        mainScreenPreferencesRepository.expandToggle(mainMenuType)
}