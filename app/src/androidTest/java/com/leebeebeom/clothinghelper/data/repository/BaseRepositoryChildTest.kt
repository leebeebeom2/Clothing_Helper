package com.leebeebeom.clothinghelper.data.repository

import androidx.test.core.app.ApplicationProvider
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.data.*
import com.leebeebeom.clothinghelper.data.repository.preference.FolderPreferencesRepositoryImpl
import com.leebeebeom.clothinghelper.domain.model.*
import com.leebeebeom.clothinghelper.domain.repository.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

const val RepositoryTestEmail = "repositorytest@a.com"
const val FolderInitialSize = 8
const val TodoInitialSize = 6

@OptIn(ExperimentalCoroutinesApi::class)
class BaseRepositoryChildTest {
    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)
    private lateinit var userRepository: UserRepository
    private lateinit var folderPreferenceRepository: FolderPreferencesRepositoryImpl
    private lateinit var folderRepository: FolderRepository
    private lateinit var todoRepository: TodoRepository

    @Before
    fun init() = runTest(dispatcher) {
        userRepository = UserRepositoryImpl(
            appScope = scope,
            dispatcherIO = dispatcher
        )
        folderPreferenceRepository = FolderPreferencesRepositoryImpl(
            context = ApplicationProvider.getApplicationContext(),
            appScope = scope
        )
        folderRepository = FolderRepositoryImpl(
            folderPreferencesRepository = folderPreferenceRepository,
            appScope = scope,
            dispatcherIO = dispatcher,
            dispatcherDefault = dispatcher,
            userRepository = userRepository
        )
        todoRepository = TodoRepositoryImpl(
            appScope = scope,
            dispatcherIO = dispatcher,
            dispatcherDefault = dispatcher,
            userRepository = userRepository
        )

        initData(
            userRepository = userRepository,
            folderRepository = folderRepository,
            todoRepository = todoRepository
        )
    }

    @After
    fun removeData() = runTest(dispatcher) {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { userRepository.userFlow.collect() }
        userRepository.signIn(email = RepositoryTestEmail, password = SignInPassword)
        waitTime()

        Firebase.database.reference.child(userRepository.userFlow.first()!!.uid).removeValue()
            .await()
        userRepository.signOut()
    }

    @Test
    fun crudTest() = runTest(dispatcher) {
        val folder = Folder(name = "zFolder add test")
        val folderEditName = "zFolder edit test"

        repositoryCrudTest(
            userRepository = userRepository,
            repository = folderRepository,
            initialSize = FolderInitialSize,
            data = folder,
            addAssert = {
                assert(it.name == folder.name)
                assert(it.parentKey == folder.parentKey)
            },
            editData = { it.copy(name = folderEditName) }) { old, new ->
            assert(new.name == folderEditName)
            assert(old.key == new.key)
            assert(old.createDate == new.createDate)
            assert(old.editDate != new.editDate)
            assert(old.parentKey == new.parentKey)
        }

        val todo = Todo(text = "todo add test", order = 5)
        val todoEditText = "todo edit test"

        repositoryCrudTest(
            userRepository = userRepository,
            repository = todoRepository,
            initialSize = TodoInitialSize,
            data = todo,
            addAssert = {
                assert(it.text == todo.text)
                assert(it.done == todo.done)
                assert(it.order == todo.order)
            },
            editData = { it.copy(text = todoEditText, done = true, order = 6) }
        ) { old, new ->
            assert(old.key == new.key)
            assert(new.done)
            assert(new.order == 6)
            assert(new.text == todoEditText)
        }
    }

    @Test
    fun orderTest() = runTest(dispatcher) {
        repositoryOrderTest(
            repository = folderRepository,
            userRepository = userRepository,
            initDataList = List(9) { Folder(name = "folder $it") }
        ) { origin, new -> assert(origin.map { it.name } == new.map { it.name }) }

        repositoryOrderTest(
            repository = todoRepository,
            userRepository = userRepository,
            initDataList = List(20) { Todo(text = "todo $it", order = it) }
        ) { origin, new -> assert(origin.map { it.text } == new.map { it.text }) }
    }

    @Test
    fun accountChangeLoadTest() = runTest(dispatcher) {
        repositoryChangeAccountLoadTest(
            repository = folderRepository,
            userRepository = userRepository,
            addDataPair = Folder(name = "folder account test 1") to Folder("folder account test 2"),
            repositoryTestAccountSize = FolderInitialSize
        )
        repositoryChangeAccountLoadTest(
            repository = todoRepository,
            userRepository = userRepository,
            addDataPair = Todo(text = "todo account test 1") to Todo(text = "todo account test 2"),
            repositoryTestAccountSize = TodoInitialSize
        )
    }

    @Test
    fun sortTest() = runTest(dispatcher) {
        var createdData = System.currentTimeMillis()

        val folders = List(9) {
            Folder(
                name = "$it",
                createDate = createdData++,
                editDate = createdData++
            )
        }.shuffled()

        folderSortTest(
            userRepository = userRepository,
            repository = folderRepository,
            preferencesRepository = folderPreferenceRepository,
            data = folders
        )

        val todos = List(9) { Todo(text = "$it", order = it) }.shuffled()
        userRepository.signIn(email = SignInEmail, password = SignInPassword)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { todoRepository.allDataFlow.collect() }
        waitTime()
        todos.map { async { todoRepository.add(it) } }.awaitAll()
        waitTime(1000)
        assert(
            todoRepository.allDataFlow.first().data.map { it.text } ==
                    todos.sortedBy { it.order }.map { it.text })
        Firebase.database.reference.child(userRepository.userFlow.first()!!.uid).removeValue()
            .await()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun TestScope.initData(
    userRepository: UserRepository,
    folderRepository: FolderRepository,
    todoRepository: TodoRepository
) {
    userRepository.signIn(email = RepositoryTestEmail, password = SignInPassword)
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
        folderRepository.allDataFlow.collect()
    }
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
        todoRepository.allDataFlow.collect()
    }
    waitTime()

    listOf(
        List(FolderInitialSize) { Folder(name = "test folder $it") }.map {
            coroutineScope { async { folderRepository.add(data = it) } }
        },
        List(TodoInitialSize) { Todo(text = "test todo $it") }.map {
            coroutineScope { async { todoRepository.add(data = it) } }
        }
    ).flatten().awaitAll()
}