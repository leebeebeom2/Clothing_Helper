package com.leebeebeom.clothinghelper.domain.usecase

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.firebase.database.FirebaseDatabase
import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.repository.FirebaseResult
import com.leebeebeom.clothinghelper.data.repository.UserRepositoryImpl
import com.leebeebeom.clothinghelper.domain.model.DatabaseFolder
import com.leebeebeom.clothinghelper.domain.model.DatabaseSubCategory
import com.leebeebeom.clothinghelper.domain.model.DatabaseTodo
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LoadDataUseCaseTest {
    private lateinit var userRepository: UserRepository
    private lateinit var subCategoryRepository: SubCategoryRepository
    private lateinit var folderRepository: FolderRepository
    private lateinit var todoRepository: TodoRepository
    private lateinit var loadDataUseCase: LoadDataUseCase
    private lateinit var getDataLoadingStateUseCase: GetDataLoadingStateUseCase

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        userRepository = UserRepositoryImpl()
        subCategoryRepository = RepositoryProvider.getSubCategoryRepository(context)
        folderRepository = RepositoryProvider.getFolderRepository(context)
        todoRepository = RepositoryProvider.getTodoRepository(context)

        loadDataUseCase = LoadDataUseCase(
            subCategoryRepository = subCategoryRepository,
            folderRepository = folderRepository,
            todoRepository = todoRepository,
            networkChecker = RepositoryProvider.getNetWorkChecker(context)
        )

        getDataLoadingStateUseCase = GetDataLoadingStateUseCase(
            subCategoryRepository,
            folderRepository,
            todoRepository,
            appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadTest() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)

        userRepository.signIn("1@a.com", "111111", object : FirebaseResult {
            override fun success() {}

            override fun fail(exception: Exception) = assert(false)
        }, dispatcher = dispatcher)

        val uid = userRepository.firebaseUser.value!!.uid

        putInitialData(uid = uid, dispatcher = dispatcher)

        loadDataUseCase.load(
            uid = uid,
            dispatcher = dispatcher
        ) { assert(false) }

        assert(subCategoryRepository.allData.value.size == 10)
        assert(folderRepository.allData.value.size == 8)
        assert(todoRepository.allData.value.size == 5)

        loadDataUseCase.load(
            uid = null,
            dispatcher = dispatcher
        ) { assert(false) }

        assert(subCategoryRepository.allData.value.isEmpty())
        assert(folderRepository.allData.value.isEmpty())
        assert(todoRepository.allData.value.isEmpty())

        FirebaseDatabase.getInstance().reference.child(uid).removeValue()
    }

    private suspend fun putInitialData(uid: String, dispatcher: CoroutineDispatcher) {
        coroutineScope {
            val data = arrayListOf<Any>().apply {
                repeat(10) { add(DatabaseSubCategory(name = "test")) }
                repeat(8) { add(DatabaseFolder(name = "test")) }
                repeat(5) { add(DatabaseTodo(text = "test", key = "0")) }
            }

            data.map {
                async {
                    when (it) {
                        is DatabaseSubCategory ->
                            subCategoryRepository.add(dispatcher, it, uid) { assert(false) }
                        is DatabaseFolder ->
                            folderRepository.add(dispatcher, it, uid) { assert(false) }
                        is DatabaseTodo ->
                            todoRepository.add(dispatcher, it, uid) { assert(false) }
                    }
                }
            }.awaitAll()
        }
    }
}