package com.leebeebeom.clothinghelperdomain.usecase.preferences.sort

import com.leebeebeom.clothinghelperdomain.repository.preferences.FolderPreferencesRepository
import com.leebeebeom.clothinghelperdomain.repository.preferences.SortPreferenceRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class FolderSortUseCase @Inject constructor(
    @FolderPreferencesRepository folderPreferencesRepository: SortPreferenceRepository
) : SortUseCase by SortUseCaseImpl(folderPreferencesRepository)