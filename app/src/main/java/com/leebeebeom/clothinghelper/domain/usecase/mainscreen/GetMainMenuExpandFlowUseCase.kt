package com.leebeebeom.clothinghelper.domain.usecase.mainscreen

import com.leebeebeom.clothinghelper.domain.repository.preference.MainScreenPreferencesRepository
import javax.inject.Inject

class GetMainMenuExpandFlowUseCase @Inject constructor(
    private val mainScreenPreferencesRepository: MainScreenPreferencesRepository
) {
    val brandExpandFlow get() = mainScreenPreferencesRepository.brandExpandFlow
    val outfitExpandFlow get() = mainScreenPreferencesRepository.outfitExpandFlow
    val clothesExpandFlow get() = mainScreenPreferencesRepository.clothesExpandFlow
}