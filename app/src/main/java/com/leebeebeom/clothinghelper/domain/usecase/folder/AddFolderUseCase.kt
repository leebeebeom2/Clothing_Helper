package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.ui.main.drawer.MainCategoryType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddFolderUseCase @Inject constructor(private val folderRepository: FolderRepository) {
    suspend fun add(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        parentKey: String,
        subCategoryKey: String,
        name: String,
        mainCategoryType: MainCategoryType,
        uid: String,
        onFail: (Exception) -> Unit,
    ) = withContext(dispatcher) {
        val folder = Folder(
            parentKey = parentKey,
            subCategoryKey = subCategoryKey,
            name = name,
            mainCategoryType = mainCategoryType
        )

        folderRepository.add(data = folder, uid = uid, onFail = onFail, dispatcher = dispatcher)
    }
}