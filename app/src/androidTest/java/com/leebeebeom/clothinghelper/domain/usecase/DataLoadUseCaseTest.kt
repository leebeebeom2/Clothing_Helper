package com.leebeebeom.clothinghelper.domain.usecase

import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.DataRepositoryTestUtil
import com.leebeebeom.clothinghelper.data.UserRepositoryTestUtil
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.domain.model.Todo
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DataLoadUseCaseTest {
    private lateinit var dataLoadUseCase: DataLoadUseCase
    private lateinit var userRepositoryTestUtil: UserRepositoryTestUtil
    private lateinit var subCategoryRepositoryTestUtil: DataRepositoryTestUtil<SubCategory, SubCategoryRepository>
    private lateinit var folderRepositoryTestUtil: DataRepositoryTestUtil<Folder, FolderRepository>
    private lateinit var todoRepositoryTestUtil: DataRepositoryTestUtil<Todo, TodoRepository>
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun init() {
        val repositoryProvider = RepositoryProvider(dispatcher)
        userRepositoryTestUtil = UserRepositoryTestUtil(repositoryProvider)
        subCategoryRepositoryTestUtil = DataRepositoryTestUtil(
            repositoryProvider = repositoryProvider,
            userRepositoryTestUtil = userRepositoryTestUtil,
            repository = repositoryProvider.createSubCategoryRepository()
        )
        folderRepositoryTestUtil = DataRepositoryTestUtil(
            repositoryProvider = repositoryProvider,
            userRepositoryTestUtil = userRepositoryTestUtil,
            repository = repositoryProvider.createFolderRepository()
        )
        todoRepositoryTestUtil = DataRepositoryTestUtil(
            repositoryProvider = repositoryProvider,
            userRepositoryTestUtil = userRepositoryTestUtil,
            repository = repositoryProvider.createTodoRepository()
        )
        dataLoadUseCase = DataLoadUseCase(
            subCategoryRepository = subCategoryRepositoryTestUtil.repository,
            folderRepository = folderRepositoryTestUtil.repository,
            todoRepository = todoRepositoryTestUtil.repository,
            networkChecker = repositoryProvider.createNetWorkChecker()
        )
    }

    @Test
    fun dataLoadTest() = runTest(dispatcher) {
        addInitData()
        userRepositoryTestUtil.userCollect(backgroundScope = backgroundScope, collect = {
            dataLoadUseCase.load(uid = it?.uid, onFail = { assert(false) })
        })

        userRepositoryTestUtil.signIn()
        advanceUntilIdle()
        userRepositoryTestUtil.assertSignIn()
        withContext(Dispatchers.Default) {
            delay(1000)
            subCategoryRepositoryTestUtil.assertAllDataSize(10)
            folderRepositoryTestUtil.assertAllDataSize(8)
            todoRepositoryTestUtil.assertAllDataSize(5)
        }

        userRepositoryTestUtil.signOut()
        advanceUntilIdle()
        userRepositoryTestUtil.assertSignOut()
        withContext(Dispatchers.Default) {
            subCategoryRepositoryTestUtil.assertAllDataIsEmpty()
            folderRepositoryTestUtil.assertAllDataIsEmpty()
            todoRepositoryTestUtil.assertAllDataIsEmpty()
        }

        userRepositoryTestUtil.signIn()
        advanceUntilIdle()
        userRepositoryTestUtil.assertSignIn()
        subCategoryRepositoryTestUtil.removeAllData()
        folderRepositoryTestUtil.removeAllData()
        todoRepositoryTestUtil.removeAllData()
    }

    private suspend fun TestScope.addInitData() {
        userRepositoryTestUtil.signIn()
        advanceUntilIdle()
        subCategoryInitData.forEach { subCategoryRepositoryTestUtil.add(it) }
        folderInitData.forEach { folderRepositoryTestUtil.add(it) }
        todoInitData.forEach { todoRepositoryTestUtil.add(it) }
        advanceUntilIdle()
        userRepositoryTestUtil.signOut()
        subCategoryRepositoryTestUtil.load(null)
        folderRepositoryTestUtil.load(null)
        todoRepositoryTestUtil.load(null)
        advanceUntilIdle()
        subCategoryRepositoryTestUtil.assertAllDataIsEmpty()
        folderRepositoryTestUtil.assertAllDataIsEmpty()
        todoRepositoryTestUtil.assertAllDataIsEmpty()
        advanceUntilIdle()
    }

    private val subCategoryInitData = List(10) { SubCategory(name = "sub category $it") }
    private val folderInitData = List(8) { Folder(name = "folder $it") }
    private val todoInitData = List(5) { Todo(text = "todo $it") }
}