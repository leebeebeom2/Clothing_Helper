package com.leebeebeom.clothinghelper.data.repository.offline

import androidx.test.core.app.ApplicationProvider
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.data.SignInPassword
import com.leebeebeom.clothinghelper.data.repository.*
import com.leebeebeom.clothinghelper.data.repository.preference.FolderPreferencesRepositoryImpl
import com.leebeebeom.clothinghelper.data.waitTime
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.model.Todo
import com.leebeebeom.clothinghelper.domain.repository.*
import com.leebeebeom.clothinghelper.isConnectedNetwork
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BaseRepositoryChildOfflineTest {
    private val dispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)
    private lateinit var userRepository: UserRepository
    private lateinit var folderRepository: FolderRepository
    private lateinit var todoRepository: TodoRepository

    @Before
    fun init() = runTest(dispatcher) {
        userRepository = UserRepositoryImpl(appScope = scope, dispatcherIO = dispatcher)
        folderRepository = FolderRepositoryImpl(
            folderPreferencesRepository = FolderPreferencesRepositoryImpl(
                context = ApplicationProvider.getApplicationContext(),
                appScope = scope
            ),
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

        if (isConnectedNetwork()) {
            userRepository.signIn(email = RepositoryTestEmail, password = SignInPassword)
            waitTime()

            initData(
                userRepository = userRepository,
                folderRepository = folderRepository,
                todoRepository = todoRepository
            )
            waitTime()
        }
    }

    @Test
    fun offlineAddTest() = runTest(dispatcher) {
        assert(!isConnectedNetwork())

        offlineLoadAndAddTest(
            repository = folderRepository,
            addDataList = Folder(name = "zFolder offline add 1") to Folder(name = "zFolder offline add 2"),
            initialSize = FolderInitialSize
        )
        offlineLoadAndAddTest(
            repository = todoRepository,
            addDataList = Todo(
                text = "todo offline add 1",
                order = 5
            ) to Todo(text = "todo offline add 2", order = 6),
            initialSize = TodoInitialSize
        )
    }

    @Test
    fun offlineEditTest() = runTest(dispatcher) {
        assert(!isConnectedNetwork())

        offlineEditTest(
            repository = folderRepository,
            initialSize = FolderInitialSize,
            getEditData = { it.copy(name = "zFolder offline edit test") },
            editAssert = { edit, edited ->
                assert(edit.name == edited.name)
            }
        )
        offlineEditTest(
            repository = todoRepository,
            initialSize = TodoInitialSize,
            getEditData = { it.copy(text = "todo offline edit test") },
            editAssert = { edit, edited ->
                assert(edit.text == edited.text)
            }
        )
    }

    @Test
    fun testCheck() = runTest(dispatcher) {
        assert(!isConnectedNetwork())

        // connect network and check the server
        launch { folderRepository.allDataFlow.collect() }
        launch { todoRepository.allDataFlow.collect() }
    }

    @Test
    fun offlineRemoveTest() = runTest(dispatcher) {
        assert(!isConnectedNetwork())

        offlineRemoveTest(
            repository = folderRepository,
            userRepository = userRepository,
            initialSize = FolderInitialSize,
            databasePath = DataBasePath.Folder
        )
        offlineRemoveTest(
            repository = todoRepository,
            userRepository = userRepository,
            initialSize = TodoInitialSize,
            databasePath = DataBasePath.Todo
        )
    }

    @Test
    fun removeData() = runTest(dispatcher) {
        assert(isConnectedNetwork())

        Firebase.database.reference.child(userRepository.userFlow.first()!!.uid).removeValue()
    }
}