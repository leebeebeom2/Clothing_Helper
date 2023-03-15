package com.leebeebeom.clothinghelper.data

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.repository.DatabasePath
import com.leebeebeom.clothinghelper.domain.model.BaseModel
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.domain.model.Todo
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import com.leebeebeom.clothinghelper.isConnectedNetwork
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BaseRepositoryChildOfflineTest {
    private val dispatcher = StandardTestDispatcher()
    private val repositoryProvider = RepositoryProvider(dispatcher)
    private val userRepositoryTestUtil = UserRepositoryTestUtil(repositoryProvider)
    private lateinit var subCategoryTestUtil: DataRepositoryTestUtil<SubCategory, SubCategoryRepository>
    private lateinit var folderTestUtil: DataRepositoryTestUtil<Folder, FolderRepository>
    private lateinit var todoTestUtil: DataRepositoryTestUtil<Todo, TodoRepository>

    @Before
    fun init() {
        subCategoryTestUtil = DataRepositoryTestUtil(
            repositoryProvider = repositoryProvider,
            repository = repositoryProvider.createSubCategoryRepository(
                userRepository = userRepositoryTestUtil.userRepository
            ),
            userRepositoryTestUtil = userRepositoryTestUtil
        )

        folderTestUtil = DataRepositoryTestUtil(
            repositoryProvider = repositoryProvider,
            repository = repositoryProvider.createFolderRepository(
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
    fun offlineTest1() = runTest(dispatcher) {
        // sign in  before test
//        userRepositoryTestUtil.signIn(email = repositoryTestEmail, password = signInPassword)
//        advanceUntilIdle()
//        wait()
//        assert(userRepositoryTestUtil.getUser()?.email == repositoryTestEmail)

        assert(!isConnectedNetwork())

        offlineLoadAndAddTest(
            repositoryTestUtil = subCategoryTestUtil,
            addDataList = SubCategory(name = "zSub category offline add 1") to SubCategory(name = "zSub category offline add 2"),
            initialSize = 10
        )
        offlineLoadAndAddTest(
            repositoryTestUtil = folderTestUtil,
            addDataList = Folder(name = "zFolder offline add 1") to Folder(name = "zFolder offline add 2"),
            initialSize = 6
        )
        offlineLoadAndAddTest(
            repositoryTestUtil = todoTestUtil,
            addDataList = Todo(
                text = "todo offline add 1",
                order = 5
            ) to Todo(text = "todo offline add 2", order = 6),
            initialSize = 5
        )
    }

    private suspend fun <T : BaseModel> TestScope.offlineLoadAndAddTest(
        repositoryTestUtil: DataRepositoryTestUtil<T, *>,
        addDataList: Pair<T, T>,
        initialSize: Int,
    ) {
        repositoryTestUtil.allDataCollect(backgroundScope)
        wait()

        assert(repositoryTestUtil.getAllData().size == initialSize)

        repositoryTestUtil.add(addDataList.first)
        advanceUntilIdle()
        wait()

        assert(repositoryTestUtil.getAllData().size == initialSize + 1)

        repositoryTestUtil.add(addDataList.second)
        advanceUntilIdle()
        wait()

        assert(repositoryTestUtil.getAllData().size == initialSize + 2)
    }

    @Test
    fun offlineTest2() = runTest(dispatcher) {
        assert(!isConnectedNetwork())

        offlineEditTest(repositoryTestUtil = subCategoryTestUtil,
            initialSize = 10,
            getEditData = { it.copy(name = "zSubCategory offline edit test") },
            editAssert = { edit, edited ->
                assert(edit.name == edited.name)
            }
        )
        offlineEditTest(repositoryTestUtil = folderTestUtil,
            initialSize = 6,
            getEditData = { it.copy(name = "zFolder offline edit test") },
            editAssert = { edit, edited ->
                assert(edit.name == edited.name)
            }
        )
        offlineEditTest(repositoryTestUtil = todoTestUtil,
            initialSize = 5,
            getEditData = { it.copy(text = "todo offline edit test") },
            editAssert = { edit, edited ->
                assert(edit.text == edited.text)
            }
        )
    }

    private suspend fun <T : BaseModel> TestScope.offlineEditTest(
        repositoryTestUtil: DataRepositoryTestUtil<T, *>,
        initialSize: Int,
        getEditData: (T) -> T,
        editAssert: (edit: T, edited: T) -> Unit,
    ) {
        repositoryTestUtil.allDataCollect(backgroundScope)
        wait()
        assert(repositoryTestUtil.getAllData().size == initialSize + 2)

        val lastData = repositoryTestUtil.getAllData().last()

        val editData = getEditData(lastData)
        repositoryTestUtil.edit(editData)
        advanceUntilIdle()
        wait()

        val editedData = repositoryTestUtil.getAllData().last()
        editAssert(editData, editedData)
    }

    @Test
    fun offlineTest3() = runTest(dispatcher) {
        // connect network and check the server
        launch { subCategoryTestUtil.repository.allData.collect {} }
        launch { folderTestUtil.repository.allData.collect {} }
        launch { todoTestUtil.repository.allData.collect {} }
    }

    @Test
    fun removeOfflineData() = runTest(dispatcher) {
        assert(!isConnectedNetwork())

        offlineDataInit(
            repositoryTestUtil = subCategoryTestUtil,
            initialSize = 10,
            databasePath = DatabasePath.SUB_CATEGORIES
        )
        offlineDataInit(
            repositoryTestUtil = folderTestUtil,
            initialSize = 6,
            databasePath = DatabasePath.FOLDERS
        )
        offlineDataInit(
            repositoryTestUtil = todoTestUtil,
            initialSize = 5,
            databasePath = DatabasePath.TODOS
        )
    }

    private suspend fun TestScope.offlineDataInit(
        repositoryTestUtil: DataRepositoryTestUtil<*, *>,
        initialSize: Int,
        databasePath: String,
    ) {
        repositoryTestUtil.allDataCollect(backgroundScope)
        val lastDataList = repositoryTestUtil.getAllData().drop(initialSize)
        assert(lastDataList.size == 2)

        val dbRef =
            Firebase.database.reference.child(userRepositoryTestUtil.getUid()!!).child(databasePath)
        dbRef.child(lastDataList.first().key).removeValue()
        dbRef.child(lastDataList.last().key).removeValue()
    }
}