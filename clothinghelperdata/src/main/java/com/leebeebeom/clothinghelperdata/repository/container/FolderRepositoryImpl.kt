package com.leebeebeom.clothinghelperdata.repository.container

import com.leebeebeom.clothinghelperdata.repository.base.DatabasePath
import com.leebeebeom.clothinghelperdomain.model.data.Folder
import com.leebeebeom.clothinghelperdomain.repository.BaseDataRepository
import com.leebeebeom.clothinghelperdomain.repository.FolderRepository
import com.leebeebeom.clothinghelperdomain.repository.preferences.FolderPreferencesRepository
import com.leebeebeom.clothinghelperdomain.repository.preferences.SortPreferenceRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderRepositoryImpl @Inject constructor(
    @FolderPreferencesRepository folderPreferencesRepository: SortPreferenceRepository
) : BaseDataRepository<Folder> by BaseDataRepositoryImpl(
    sortFlow = folderPreferencesRepository.sort,
    refPath = DatabasePath.FOLDERS
), FolderRepository