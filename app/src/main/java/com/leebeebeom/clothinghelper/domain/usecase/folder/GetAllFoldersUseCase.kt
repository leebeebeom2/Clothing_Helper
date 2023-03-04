package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.usecase.BaseGetAllDataUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllFoldersUseCase @Inject constructor(folderRepository: FolderRepository) :
    BaseGetAllDataUseCase<Folder>(repository = folderRepository)