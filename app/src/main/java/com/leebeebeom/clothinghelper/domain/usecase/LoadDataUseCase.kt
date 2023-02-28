package com.leebeebeom.clothinghelper.domain.usecase

import com.leebeebeom.clothinghelper.data.repository.util.NetworkChecker
import com.leebeebeom.clothinghelper.data.repository.util.logE
import com.leebeebeom.clothinghelper.domain.model.DatabaseFolder
import com.leebeebeom.clothinghelper.domain.model.DatabaseSubCategory
import com.leebeebeom.clothinghelper.domain.model.DatabaseTodo
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class LoadDataUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val subCategoryRepository: SubCategoryRepository,
    private val folderRepository: FolderRepository,
    private val todoRepository: TodoRepository,
    private val networkChecker: NetworkChecker,
) {
    suspend fun load(onFail: (Exception) -> Unit) {
        userRepository.firebaseUser.collectLatest {
            try {
                networkChecker.checkNetWork()

                coroutineScope {

                    val deferred = listOf(
                        async {
                            subCategoryRepository.load(
                                uid = it?.uid,
                                type = DatabaseSubCategory::class.java,
                                onFail = { throw it }
                            )
                        },
                        async {
                            folderRepository.load(
                                uid = it?.uid,
                                type = DatabaseFolder::class.java,
                                onFail = { throw it }
                            )
                        },
                        async {
                            todoRepository.load(
                                uid = it?.uid,
                                type = DatabaseTodo::class.java,
                                onFail = { throw it }
                            )
                        }
                    )
                    deferred.awaitAll()
                }
            } catch (e: Exception) {
                onFail(e)
                logE("LoadDataUseCase:", e)
            }
        }
    }
}