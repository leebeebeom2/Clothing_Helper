package com.leebeebeom.clothinghelper.data.repository.offline

import androidx.test.core.app.ApplicationProvider
import com.leebeebeom.clothinghelper.data.repository.*
import com.leebeebeom.clothinghelper.data.repository.container.FolderRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.container.SubCategoryRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.preference.FolderPreferencesRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.preference.SubCategoryPreferencesRepositoryImpl
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.domain.model.Todo
import com.leebeebeom.clothinghelper.domain.repository.*
import com.leebeebeom.clothinghelper.isConnectedNetwork
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BaseRepositoryChildOfflineTest {
    private val dispatcher = StandardTestDispatcher()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private lateinit var userRepository: UserRepository
    private lateinit var subCategoryRepository: SubCategoryRepository
    private lateinit var folderRepository: FolderRepository
    private lateinit var todoRepository: TodoRepository

    @Before
    fun init() {
        userRepository = UserRepositoryImpl(appScope = scope, dispatcherIO = dispatcher)
        subCategoryRepository = SubCategoryRepositoryImpl(
            subCategoryPreferencesRepository = SubCategoryPreferencesRepositoryImpl(
                context = ApplicationProvider.getApplicationContext(),
                appScope = scope
            ),
            appScope = scope,
            dispatcherIO = dispatcher,
            dispatcherDefault = dispatcher,
            userRepository = userRepository
        )
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
    }

    @Test
    fun offlineTest1() = runTest(dispatcher) {
//        userRepository.signIn(email = RepositoryTestEmail, password = SignInPassword)
//        waitTime()

        assert(!isConnectedNetwork())

        offlineLoadAndAddTest(
            repository = subCategoryRepository,
            addDataList = SubCategory(name = "zSub category offline add 1") to SubCategory(name = "zSub category offline add 2"),
            initialSize = SubCategoryInitialSize
        )
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
    fun offlineTest2() = runTest(dispatcher) {
        assert(!isConnectedNetwork())

        offlineEditTest(
            repository = subCategoryRepository,
            initialSize = SubCategoryInitialSize,
            getEditData = { it.copy(name = "zSubCategory offline edit test") },
            editAssert = { edit, edited ->
                assert(edit.name == edited.name)
            }
        )
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
    fun offlineTest3() = runTest(dispatcher) {
        // connect network and check the server
        launch { subCategoryRepository.allDataFlow.collect() }
        launch { folderRepository.allDataFlow.collect() }
        launch { todoRepository.allDataFlow.collect() }
    }

    @Test
    fun removeOfflineData() = runTest(dispatcher) {
        assert(!isConnectedNetwork())

        removeOfflineData(
            repository = subCategoryRepository,
            userRepository = userRepository,
            initialSize = SubCategoryInitialSize,
            databasePath = DataBasePath.SubCategory
        )
        removeOfflineData(
            repository = folderRepository,
            userRepository = userRepository,
            initialSize = FolderInitialSize,
            databasePath = DataBasePath.Folder
        )
        removeOfflineData(
            repository = todoRepository,
            userRepository = userRepository,
            initialSize = TodoInitialSize,
            databasePath = DataBasePath.Todo
        )
    }
}