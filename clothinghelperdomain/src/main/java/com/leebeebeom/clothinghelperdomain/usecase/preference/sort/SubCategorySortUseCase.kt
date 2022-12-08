package com.leebeebeom.clothinghelperdomain.usecase.preference.sort

import com.leebeebeom.clothinghelperdomain.repository.preference.SortPreferenceRepository
import com.leebeebeom.clothinghelperdomain.repository.preference.SubCategoryPreferencesRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SubCategorySortUseCase @Inject constructor(
    @SubCategoryPreferencesRepository subCategoryPreferencesRepository: SortPreferenceRepository
) : SortUseCase by SortUseCaseImpl(sortPreferenceRepository = subCategoryPreferencesRepository)