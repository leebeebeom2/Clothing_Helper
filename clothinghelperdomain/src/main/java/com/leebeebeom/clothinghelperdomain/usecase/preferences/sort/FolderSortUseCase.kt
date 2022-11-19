package com.leebeebeom.clothinghelperdomain.usecase.preferences.sort

import com.leebeebeom.clothinghelperdomain.repository.preferences.FolderPreferencesRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class FolderSortUseCase @Inject constructor(folderPreferencesRepository: FolderPreferencesRepository) :
    SortUseCase by SortUseCaseImpl(folderPreferencesRepository)