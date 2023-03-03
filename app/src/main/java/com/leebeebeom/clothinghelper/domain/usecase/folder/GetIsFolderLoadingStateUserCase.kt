package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.di.AppScope
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.usecase.BaseGetIsDataLoadingStateUseCase
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetIsFolderLoadingStateUserCase @Inject constructor(
    folderRepository: FolderRepository,
    @AppScope appScope: CoroutineScope,
) : BaseGetIsDataLoadingStateUseCase(repository = folderRepository, appScope = appScope)