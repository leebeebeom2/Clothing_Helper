package com.leebeebeom.clothinghelper.domain.usecase

import com.leebeebeom.clothinghelper.data.repository.util.NetworkChecker
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class DataLoadUseCase @Inject constructor(
    private val subCategoryRepository: SubCategoryRepository,
    private val folderRepository: FolderRepository,
    private val todoRepository: TodoRepository,
    private val networkChecker: NetworkChecker,
) {
    suspend fun load(uid: String?, onFail: (Exception) -> Unit) {
        try {
            coroutineScope {
                networkChecker.checkNetWork()

                launch { subCategoryRepository.load(uid = uid, onFail = { throw it }) }
                launch { folderRepository.load(uid = uid, onFail = { throw it }) }
                launch { todoRepository.load(uid = uid, onFail = { throw it }) }
            }
        } catch (e: Exception) {
            onFail(e)
        }
    }
}