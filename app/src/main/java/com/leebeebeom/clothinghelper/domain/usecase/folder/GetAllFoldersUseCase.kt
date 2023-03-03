package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.di.AppScope
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.usecase.BaseGetAllDataUseCase
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllFoldersUseCase @Inject constructor(
    folderRepository: FolderRepository,
    @AppScope appScope: CoroutineScope,
) : BaseGetAllDataUseCase<Folder>(repository = folderRepository, appScope = appScope)