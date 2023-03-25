package com.leebeebeom.clothinghelper.data.repository

import androidx.test.core.app.ApplicationProvider
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.data.*
import com.leebeebeom.clothinghelper.data.repository.container.FolderRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.container.SubCategoryRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.preference.FolderPreferencesRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.preference.SubCategoryPreferencesRepositoryImpl
import com.leebeebeom.clothinghelper.domain.model.*
import com.leebeebeom.clothinghelper.domain.repository.*
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Test

const val RepositoryTestEmail = "repositorytest@a.com"
const val SubCategoryInitialSize = 8
const val FolderInitialSize = 6
const val TodoInitialSize = 5

@OptIn(ExperimentalCoroutinesApi::class)
class BaseRepositoryChildTest {
    private val dispatcher = StandardTestDispatcher()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private lateinit var userRepository: UserRepository
    private lateinit var subCategoryPreferenceRepository: SortPreferenceRepository
    private lateinit var subCategoryRepository: SubCategoryRepository
    private lateinit var folderPreferenceRepository: SortPreferenceRepository
    private lateinit var folderRepository: FolderRepository
    private lateinit var todoRepository: TodoRepository

    @Before
    fun init() {
        userRepository = UserRepositoryImpl(appScope = scope, dispatcher = dispatcher)
        subCategoryPreferenceRepository = SubCategoryPreferencesRepositoryImpl(
            context = ApplicationProvider.getApplicationContext(),
            appScope = scope
        )
        subCategoryRepository = SubCategoryRepositoryImpl(
            subCategoryPreferencesRepository = subCategoryPreferenceRepository,
            appScope = scope,
            dispatcher = dispatcher,
            userRepository = userRepository
        )
        folderPreferenceRepository = FolderPreferencesRepositoryImpl(
            context = ApplicationProvider.getApplicationContext(),
            appScope = scope
        )
        folderRepository = FolderRepositoryImpl(
            folderPreferencesRepository = folderPreferenceRepository,
            appScope = scope,
            dispatcher = dispatcher,
            userRepository = userRepository
        )
        todoRepository = TodoRepositoryImpl(
            appScope = scope,
            dispatcher = dispatcher,
            userRepository = userRepository
        )
    }

    @Test
    fun crudTest() = runTest(dispatcher) {
        val subCategory = SubCategory(name = "zSubCategory add test")
        val subCategoryEditName = "zSubCategory edit test"

        repositoryCrudTest(
            userRepository = userRepository,
            repository = subCategoryRepository,
            initialSize = SubCategoryInitialSize,
            refPath = DataBasePath.SubCategory,
            data = subCategory,
            addAssert = { assert(it.name == subCategory.name) },
            editData = { it.copy(name = subCategoryEditName) }) { old, new ->
            assert(old.key == new.key)
            assert(old.mainCategoryType == new.mainCategoryType)
            assert(old.createDate == new.createDate)
            assert(old.editDate != new.editDate)
            assert(old.editDate < new.editDate)
            assert(new.name == subCategoryEditName)
        }

        val folder = Folder(name = "zFolder add test")
        val folderEditName = "zFolder edit test"

        repositoryCrudTest(
            userRepository = userRepository,
            repository = folderRepository,
            initialSize = FolderInitialSize,
            refPath = DataBasePath.Folder,
            data = folder,
            addAssert = { assert(it.name == folder.name) },
            editData = { it.copy(name = folderEditName) }) { old, new ->
            assert(new.name == folderEditName)
            assert(old.key == new.key)
            assert(old.createDate == new.createDate)
            assert(old.editDate != new.editDate)
            assert(old.mainCategoryType == new.mainCategoryType)
            assert(old.parentKey == new.parentKey)
            assert(old.subCategoryKey == new.subCategoryKey)
        }

        val todo = Todo(text = "todo add test", order = 5)
        val todoEditText = "todo edit test"

        repositoryCrudTest(
            userRepository = userRepository,
            repository = todoRepository,
            initialSize = TodoInitialSize,
            refPath = DataBasePath.Todo,
            data = todo,
            addAssert = {
                assert(it.text == todo.text)
                assert(it.done == todo.done)
                assert(it.order == todo.order)
            },
            editData = { it.copy(text = todoEditText, done = true, order = 6) },
            editAssert = { old, new ->
                assert(old.key == new.key)
                assert(new.done)
                assert(new.order == 6)
                assert(new.text == todoEditText)
            }
        )
    }

    @Test
    fun orderTest() = runTest(dispatcher) {
        repositoryOrderTest(
            repository = subCategoryRepository,
            userRepository = userRepository,
            initDataList = List(9) { SubCategory(name = "subCategory $it") },
            assertOrder = { origin, new -> assert(origin.map { it.name } == new.map { it.name }) },
            refPath = DataBasePath.SubCategory
        )
        repositoryOrderTest(
            repository = folderRepository,
            userRepository = userRepository,
            initDataList = List(9) { Folder(name = "folder $it") },
            assertOrder = { origin, new -> assert(origin.map { it.name } == new.map { it.name }) },
            refPath = DataBasePath.Folder
        )
        repositoryOrderTest(
            repository = todoRepository,
            userRepository = userRepository,
            initDataList = List(20) { Todo(text = "todo $it", order = it) },
            assertOrder = { origin, new -> assert(origin.map { it.text } == new.map { it.text }) },
            refPath = DataBasePath.Todo
        )
    }

    @Test
    fun accountChangeLoadTest() = runTest(dispatcher) {
        repositoryChangeAccountLoadTest(
            repository = subCategoryRepository,
            userRepository = userRepository,
            addDataPair =
            SubCategory(name = "subCategory account test 1") to SubCategory("subCategory account test 2"),
            repositoryTestAccountSize = SubCategoryInitialSize,
            refPath = DataBasePath.SubCategory
        )
        repositoryChangeAccountLoadTest(
            repository = folderRepository,
            userRepository = userRepository,
            addDataPair = Folder(name = "folder account test 1") to Folder("folder account test 2"),
            repositoryTestAccountSize = FolderInitialSize,
            refPath = DataBasePath.Folder
        )
        repositoryChangeAccountLoadTest(
            repository = todoRepository,
            userRepository = userRepository,
            addDataPair = Todo(text = "todo account test 1") to Todo(text = "todo account test 2"),
            repositoryTestAccountSize = TodoInitialSize,
            refPath = DataBasePath.Todo
        )
    }

    @Test
    fun sortTest() = runTest(dispatcher) {
        var createdData = System.currentTimeMillis()

        val subCategories = List(9) {
            SubCategory(
                name = "$it",
                createDate = createdData++,
                editDate = createdData++
            )
        }.shuffled()
        containerSortTest(
            userRepository = userRepository,
            repository = subCategoryRepository,
            preferencesRepository = subCategoryPreferenceRepository,
            data = subCategories
        )

        val folders = List(9) {
                Folder(
                    name = "$it",
                    createDate = createdData++,
                    editDate = createdData++
                )
            }.shuffled()

        containerSortTest(
            userRepository = userRepository,
            repository = folderRepository,
            preferencesRepository = folderPreferenceRepository,
            data = folders
        )

        val todos = List(9) { Todo(text = "$it", order = it) }.shuffled()
        userRepository.signIn(email = SignInEmail, password = SignInPassword)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { todoRepository.allDataStream.collect() }
        waitTime()
        todos.forEach { todoRepository.add(it) }
        advanceUntilIdle()
        waitTime(2000)
        println("${todoRepository.allDataStream.first().data.map { it.text }}")
        println("${todos.sortedBy { it.order }.map { it.text }}")
        assert(
            todoRepository.allDataStream.first().data.map { it.text } ==
                    todos.sortedBy { it.order }.map { it.text })
        Firebase.database.reference.child(userRepository.userStream.first()!!.uid).removeValue()
            .await()
    }
}