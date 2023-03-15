package com.leebeebeom.clothinghelper.data

import android.util.Log
import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.repository.preference.Order
import com.leebeebeom.clothinghelper.data.repository.preference.Sort
import com.leebeebeom.clothinghelper.domain.model.*
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

const val repositoryTestEmail = "repositorytest@a.com"

@OptIn(ExperimentalCoroutinesApi::class)
class BaseRepositoryChildTest {
    private lateinit var subCategoryTestUtil: DataRepositoryTestUtil<SubCategory, SubCategoryRepository>
    private lateinit var folderTestUtil: DataRepositoryTestUtil<Folder, FolderRepository>
    private lateinit var todoTestUtil: DataRepositoryTestUtil<Todo, TodoRepository>

    private val dispatcher = StandardTestDispatcher()
    private val repositoryProvider = RepositoryProvider(dispatcher)
    private val userRepositoryTestUtil = UserRepositoryTestUtil(repositoryProvider)
    private val subCategoryPreferencesRepository =
        repositoryProvider.createSubCategoryPreferenceRepository()
    private val folderPreferencesRepository = repositoryProvider.createFolderPreferenceRepository()

    @Before
    fun init() {
        subCategoryTestUtil = DataRepositoryTestUtil(
            repositoryProvider = repositoryProvider,
            repository = repositoryProvider.createSubCategoryRepository(
                subCategoryPreferencesRepository = subCategoryPreferencesRepository,
                userRepository = userRepositoryTestUtil.userRepository
            ),
            userRepositoryTestUtil = userRepositoryTestUtil
        )
        folderTestUtil = DataRepositoryTestUtil(
            repositoryProvider = repositoryProvider,
            repository = repositoryProvider.createFolderRepository(
                folderPreferencesRepository = folderPreferencesRepository,
                userRepository = userRepositoryTestUtil.userRepository
            ),
            userRepositoryTestUtil = userRepositoryTestUtil
        )
        todoTestUtil = DataRepositoryTestUtil(
            repositoryProvider = repositoryProvider,
            repository = repositoryProvider.createTodoRepository(userRepository = userRepositoryTestUtil.userRepository),
            userRepositoryTestUtil = userRepositoryTestUtil
        )
    }

    @Test
    fun crudTest() = runTest(dispatcher) {
        val subCategory = SubCategory(name = "subCategory test")

        repositoryCrudTest(testUtil = subCategoryTestUtil, data = subCategory, addAssert = {
            assert(it.name == subCategory.name)
        }, editData = { it.copy(name = "subCategory edit") }) { old, new ->
            assert(old.key == new.key)
            assert(old.mainCategoryType == new.mainCategoryType)
            assert(old.createDate == new.createDate)
            assert(old.editDate != new.editDate)
            assert(old.editDate < new.editDate)
            assert(new.name == "subCategory edit")
        }

        val folder = Folder(name = "folder test")

        repositoryCrudTest(testUtil = folderTestUtil, data = folder, addAssert = {
            assert(it.name == folder.name)
        }, editData = { it.copy(name = "folder edit test") }) { old, new ->
            assert(new.name == "folder edit test")
            assert(old.key == new.key)
            assert(old.createDate == new.createDate)
            assert(old.editDate != new.editDate)
            assert(old.mainCategoryType == new.mainCategoryType)
            assert(old.parentKey == new.parentKey)
            assert(old.subCategoryKey == new.subCategoryKey)
        }

        val todo = Todo(text = "todo")

        repositoryCrudTest(testUtil = todoTestUtil,
            data = todo,
            addAssert = {
                assert(it.text == todo.text)
                assert(it.done == todo.done)
                assert(it.order == todo.order)
            },
            editData = { it.copy(text = "new todo", done = true, order = 1) }
        ) { old, new ->
            assert(old.key == new.key)
            assert(new.done)
            assert(new.order == 1)
            assert(new.text == "new todo")
        }
    }

    @Test
    fun orderTest() = runTest(dispatcher) {
        repositoryOrderTest(testUtil = subCategoryTestUtil,
            initDataList = List(9) { SubCategory(name = "subCategory $it") },
            assertOrder = { origin, new ->
                assert(origin.map { it.name } == new.map { it.name })
            }

        )
        repositoryOrderTest(testUtil = folderTestUtil,
            initDataList = List(9) { Folder(name = "folder $it") },
            assertOrder = { origin, new -> assert(origin.map { it.name } == new.map { it.name }) })

        repositoryOrderTest(testUtil = todoTestUtil,
            initDataList = List(20) { Todo(text = "todo $it", order = it) },
            assertOrder = { origin, new ->
                assert(origin.map { it.text } == new.map { it.text })
            })
    }

    @Test
    fun loadingTest() = runTest(dispatcher) {
        // check the logcat
        repositoryLoadingTest(subCategoryTestUtil)
        repositoryLoadingTest(folderTestUtil)
        repositoryLoadingTest(todoTestUtil)
    }

    @Test
    fun loadTest() = runTest(dispatcher) {
        repositoryLoadTest(
            testUtil = subCategoryTestUtil,
            addDataPair = SubCategory(name = "subCategory test 1") to SubCategory("subCategory test 2"),
            repositoryTestAccountSize = 10
        )
        repositoryLoadTest(
            testUtil = folderTestUtil,
            addDataPair = Folder(name = "folder test 1") to Folder("folder test 2"),
            repositoryTestAccountSize = 6
        )
        repositoryLoadTest(
            testUtil = todoTestUtil,
            addDataPair = Todo(text = "todo test 1") to Todo(text = "todo test 2"),
            repositoryTestAccountSize = 5
        )
    }

    @Test
    fun containerSortTest() = runTest(dispatcher) {
        var createdData = System.currentTimeMillis()

        val subCategories = List(9) {
            SubCategory(
                name = "$it", createDate = createdData++, editDate = createdData++
            )
        }
        containerSortTest(
            testUtil = subCategoryTestUtil,
            preferencesRepository = subCategoryPreferencesRepository,
            data = subCategories
        )

        val folders =
            List(9) { Folder(name = "$it", createDate = createdData++, editDate = createdData++) }

        containerSortTest(
            testUtil = folderTestUtil,
            preferencesRepository = folderPreferencesRepository,
            data = folders
        )

        val todos = List(9) { Todo(text = "$it", order = it) }
        todoTestUtil.allDataCollect(backgroundScope)
        advanceUntilIdle()
        wait()

        todos.shuffled().forEach { todoTestUtil.add(it) }
        advanceUntilIdle()
        wait()
        assert(todoTestUtil.getAllData().map { it.text } == todos.map { it.text })
        todoTestUtil.removeAllData()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
private suspend fun <T : BaseModel, U : BaseDataRepository<T>> TestScope.repositoryCrudTest(
    testUtil: DataRepositoryTestUtil<T, U>,
    data: T,
    addAssert: (T) -> Unit,
    editData: (oldData: T) -> T,
    editAssert: (old: T, new: T) -> Unit,
) {
    val userRepositoryTestUtil = testUtil.userRepositoryTestUtil

    userRepositoryTestUtil.signIn()
    advanceUntilIdle()

    testUtil.allDataCollect(backgroundScope = backgroundScope)
    wait()

    assert(testUtil.getAllData().isEmpty())

    testUtil.add(data = data)
    advanceUntilIdle()
    wait()
    assert(testUtil.getAllData().size == 1)

    val addedData = testUtil.getFirstData()
    addAssert(addedData)

    testUtil.edit(newData = editData(addedData))
    advanceUntilIdle()
    wait()
    val editedData = testUtil.getFirstData()
    editAssert(addedData, editedData)

    userRepositoryTestUtil.signOut()
    advanceUntilIdle()
    wait()
    assert(testUtil.getAllData().isEmpty())

    userRepositoryTestUtil.signIn()
    advanceUntilIdle()
    wait()
    assert(editedData == testUtil.getFirstData())

    testUtil.removeAllData()
}

@OptIn(ExperimentalCoroutinesApi::class)
private suspend fun <T : BaseModel> TestScope.repositoryOrderTest(
    testUtil: DataRepositoryTestUtil<T, *>,
    initDataList: List<T>,
    assertOrder: (origin: List<T>, new: List<T>) -> Unit,
) {
    testUtil.allDataCollect(backgroundScope)

    testUtil.userRepositoryTestUtil.signIn()
    advanceUntilIdle()
    wait()
    assert(testUtil.getAllData().isEmpty())

    initDataList.shuffled().forEach { testUtil.add(it) }
    advanceUntilIdle()
    wait()
    assertOrder(initDataList, testUtil.getAllData())

    testUtil.removeAllData()
}

@OptIn(ExperimentalCoroutinesApi::class)
private suspend fun TestScope.repositoryLoadingTest(testUtil: DataRepositoryTestUtil<*, *>) {
    val userRepositoryTestUtil = testUtil.userRepositoryTestUtil

    userRepositoryTestUtil.signOut()
    wait()

    Log.d(dataLoadingTag, "loadingCollect start")
    testUtil.loadingCollect(backgroundScope = backgroundScope)
    Log.d(dataLoadingTag, "allDataCollect start")
    testUtil.allDataCollect(backgroundScope = backgroundScope)
    wait()

    Log.d(dataLoadingTag, "sign in start")
    userRepositoryTestUtil.signIn()
    advanceUntilIdle()
    wait()

    Log.d(dataLoadingTag, "sign out start")
    userRepositoryTestUtil.signOut()
    wait()

    Log.d(dataLoadingTag, "sign in 2  start")
    userRepositoryTestUtil.signIn(email = repositoryTestEmail, password = signInPassword)
    advanceUntilIdle()
    wait()

    Log.d(dataLoadingTag, "sign out 2 start")
    userRepositoryTestUtil.signOut()
    wait()
}

@OptIn(ExperimentalCoroutinesApi::class)
private suspend fun <T : BaseModel> TestScope.repositoryLoadTest(
    testUtil: DataRepositoryTestUtil<T, *>,
    addDataPair: Pair<T, T>,
    repositoryTestAccountSize: Int,
) {
    val userRepositoryTestUtil = testUtil.userRepositoryTestUtil

    testUtil.allDataCollect(backgroundScope)

    userRepositoryTestUtil.signOut()
    wait()

    assert(testUtil.getAllData().isEmpty())

    userRepositoryTestUtil.signIn()
    advanceUntilIdle()
    wait()
    assert(testUtil.getAllData().isEmpty())

    testUtil.add(addDataPair.first)
    testUtil.add(addDataPair.second)
    advanceUntilIdle()
    wait()

    assert(testUtil.getAllData().size == 2)

    userRepositoryTestUtil.signOut()
    wait()
    assert(testUtil.getAllData().isEmpty())

    userRepositoryTestUtil.signIn(repositoryTestEmail, signInPassword)
    advanceUntilIdle()
    wait()

    assert(testUtil.getAllData().size == repositoryTestAccountSize)

    userRepositoryTestUtil.signOut()
    wait()
    assert(testUtil.getAllData().isEmpty())

    userRepositoryTestUtil.signIn()
    advanceUntilIdle()
    wait()
    assert(testUtil.getAllData().size == 2)

    testUtil.removeAllData()
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T : BaseContainerModel, U : BaseDataRepository<T>> TestScope.containerSortTest(
    testUtil: DataRepositoryTestUtil<T, U>,
    preferencesRepository: SortPreferenceRepository,
    data: List<T>,
) {
    val userRepositoryTestUtil = testUtil.userRepositoryTestUtil

    suspend fun sortInit() {
        preferencesRepository.changeSort(Sort.Name)
        preferencesRepository.changeOrder(Order.Ascending)
    }

    sortInit()

    userRepositoryTestUtil.signIn()
    advanceUntilIdle()
    wait()

    testUtil.allDataCollect(backgroundScope)
    advanceUntilIdle()
    wait()
    assert(testUtil.getAllData().isEmpty())

    data.forEach { testUtil.add(it) }
    advanceUntilIdle()
    wait()
    assert(testUtil.getAllData().size == data.size)

    suspend fun assertSort(firstItemName: String, lastItemName: String) {
        assert(testUtil.getFirstData().name == firstItemName)
        assert(testUtil.getAllData().last().name == lastItemName)
    }

    assertSort(firstItemName = "0", lastItemName = "8") // Sort: Name, Order: Ascending

    preferencesRepository.changeOrder(Order.Descending)
    advanceUntilIdle()
    assertSort(firstItemName = "8", lastItemName = "0") // Sort: Name, Order: Descending

    preferencesRepository.changeOrder(Order.Ascending)
    advanceUntilIdle()
    assertSort(firstItemName = "0", lastItemName = "8") // Sort: Name, Order: Ascending

    preferencesRepository.changeSort(Sort.Create)
    advanceUntilIdle()
    assertSort(firstItemName = "0", lastItemName = "8") // Sort: CREATE, Order: Ascending

    preferencesRepository.changeOrder(Order.Descending)
    advanceUntilIdle()
    assertSort(firstItemName = "8", lastItemName = "0") // Sort: CREATE, Order: Descending

    preferencesRepository.changeOrder(Order.Ascending)
    advanceUntilIdle()
    assertSort(firstItemName = "0", lastItemName = "8") // Sort: CREATE, Order: Ascending

    preferencesRepository.changeSort(Sort.Edit)
    advanceUntilIdle()
    assertSort(firstItemName = "0", lastItemName = "8") // Sort: EDIT, Order: Ascending

    preferencesRepository.changeOrder(Order.Descending)
    advanceUntilIdle()
    assertSort(firstItemName = "8", lastItemName = "0")  // Sort: EDIT, Order: Descending

    preferencesRepository.changeOrder(Order.Ascending)
    advanceUntilIdle()
    assertSort(firstItemName = "0", lastItemName = "8")  // Sort: EDIT, Order: Ascending

    sortInit()

    testUtil.removeAllData()
}