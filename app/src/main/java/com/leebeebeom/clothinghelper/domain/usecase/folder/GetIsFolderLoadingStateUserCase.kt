package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.usecase.BaseGetIsDataLoadingStateUseCase
import javax.inject.Inject

class GetIsFolderLoadingStateUserCase @Inject constructor(
    folderRepository: FolderRepository,
) : BaseGetIsDataLoadingStateUseCase(repository = folderRepository)