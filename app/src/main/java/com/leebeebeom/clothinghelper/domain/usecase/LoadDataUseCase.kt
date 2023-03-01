package com.leebeebeom.clothinghelper.domain.usecase

import com.google.firebase.FirebaseNetworkException
import com.leebeebeom.clothinghelper.data.repository.util.NetworkChecker
import com.leebeebeom.clothinghelper.data.repository.util.WifiException
import com.leebeebeom.clothinghelper.data.repository.util.logE
import com.leebeebeom.clothinghelper.domain.model.DatabaseFolder
import com.leebeebeom.clothinghelper.domain.model.DatabaseSubCategory
import com.leebeebeom.clothinghelper.domain.model.DatabaseTodo
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import kotlinx.coroutines.*
import javax.inject.Inject

class LoadDataUseCase @Inject constructor(
    private val subCategoryRepository: SubCategoryRepository,
    private val folderRepository: FolderRepository,
    private val todoRepository: TodoRepository,
    private val networkChecker: NetworkChecker,
) {
    /**
     * @throws FirebaseNetworkException 인터넷 미 연결 시
     * @throws WifiException 사용자가 와이파이로만 연결 선택 시 와이파이 미 연결됐을 경우
     */
    suspend fun load(
        uid: String?,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        onFail: (Exception) -> Unit,
    ) {
        try {
            coroutineScope {
                networkChecker.checkNetWork()

                val deferred = listOf(
                    async {
                        subCategoryRepository.load(
                            dispatcher = dispatcher,
                            uid = uid,
                            type = DatabaseSubCategory::class.java,
                            onFail = { throw it }
                        )
                    },
                    async {
                        folderRepository.load(
                            dispatcher = dispatcher,
                            uid = uid,
                            type = DatabaseFolder::class.java,
                            onFail = { throw it }
                        )
                    },
                    async {
                        todoRepository.load(
                            dispatcher = dispatcher,
                            uid = uid,
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