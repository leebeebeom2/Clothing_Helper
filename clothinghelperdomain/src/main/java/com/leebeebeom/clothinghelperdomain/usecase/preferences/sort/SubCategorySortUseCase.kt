package com.leebeebeom.clothinghelperdomain.usecase.preferences.sort

import com.leebeebeom.clothinghelperdomain.repository.preferences.SortPreferenceRepository
import com.leebeebeom.clothinghelperdomain.repository.preferences.SubCategoryPreferencesRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SubCategorySortUseCase @Inject constructor(
    @SubCategoryPreferencesRepository subCategoryPreferencesRepository: SortPreferenceRepository
) : SortUseCase by SortUseCaseImpl(sortPreferenceRepository = subCategoryPreferencesRepository)