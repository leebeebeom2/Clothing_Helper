package com.leebeebeom.clothinghelperdomain.usecase.preferences.sort

import com.leebeebeom.clothinghelperdomain.repository.preferences.SubCategoryPreferencesRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SubCategorySortUseCase
@Inject constructor(subCategoryPreferencesRepository: SubCategoryPreferencesRepository) :
    SortUseCase by SortUseCaseImpl(subCategoryPreferencesRepository)