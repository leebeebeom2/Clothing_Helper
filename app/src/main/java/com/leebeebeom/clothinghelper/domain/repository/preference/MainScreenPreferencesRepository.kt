package com.leebeebeom.clothinghelper.domain.repository.preference

import com.leebeebeom.clothinghelper.ui.drawer.content.mainmenu.MainMenuType
import kotlinx.coroutines.flow.SharedFlow

interface MainScreenPreferencesRepository {
    val brandExpandFlow: SharedFlow<Boolean>
    val outfitExpandFlow: SharedFlow<Boolean>
    val clothesExpandFlow: SharedFlow<Boolean>

    suspend fun expandToggle(mainMenuType: MainMenuType)
}