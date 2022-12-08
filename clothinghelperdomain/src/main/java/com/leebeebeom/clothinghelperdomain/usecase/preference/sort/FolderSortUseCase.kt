package com.leebeebeom.clothinghelperdomain.usecase.preference.sort

import com.leebeebeom.clothinghelperdomain.repository.preference.FolderPreferencesRepository
import com.leebeebeom.clothinghelperdomain.repository.preference.SortPreferenceRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class FolderSortUseCase @Inject constructor(
    @FolderPreferencesRepository folderPreferencesRepository: SortPreferenceRepository
) : SortUseCase by SortUseCaseImpl(sortPreferenceRepository = folderPreferencesRepository)