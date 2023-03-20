package com.leebeebeom.clothinghelper.domain.usecase.preference.sort.subcategory

import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SubCategoryPreferencesRepository
import com.leebeebeom.clothinghelper.domain.usecase.preference.sort.BaseChangeOrderUseCase
import javax.inject.Inject

class ChangeSubCategoryOrderUseCase @Inject constructor(@SubCategoryPreferencesRepository sortPreferenceRepository: SortPreferenceRepository) :
    BaseChangeOrderUseCase(sortPreferenceRepository)