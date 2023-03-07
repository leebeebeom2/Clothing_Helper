@file:OptIn(ExperimentalCoroutinesApi::class)

package com.leebeebeom.clothinghelper.data

import com.google.firebase.database.FirebaseDatabase
import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.repository.preference.Order
import com.leebeebeom.clothinghelper.data.repository.preference.Sort
import com.leebeebeom.clothinghelper.domain.model.BaseContainerModel
import com.leebeebeom.clothinghelper.domain.model.BaseModel
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class DataRepositoryTestUtil<T : BaseModel, U : BaseDataRepository<T>>(
    repositoryProvider: RepositoryProvider,
    val repository: U,
    val userRepositoryTestUtil: UserRepositoryTestUtil = UserRepositoryTestUtil(repositoryProvider = repositoryProvider),
) {
    val dispatcher = repositoryProvider.dispatcher
    val allData get() = repository.allData.value

    suspend fun allDataCollect(backgroundScope: CoroutineScope) {
        backgroundScope.launch(dispatcher) {
            userRepositoryTestUtil.userCollect(backgroundScope) {
                load(uid = it?.uid)
            }
        }
        backgroundScope.launch(dispatcher) { repository.allData.collectLatest { } }
    }

    suspend fun load(uid: String? = userRepositoryTestUtil.uid) = repository.load(
        uid = uid, onFail = { assert(false) }
    )

    suspend fun add(data: T) =
        repository.add(data = data, uid = userRepositoryTestUtil.uid!!, onFail = { assert(false) })

    suspend fun edit(oldData: T, newData: T) = repository.edit(oldData = oldData,
        newData = newData,
        uid = userRepositoryTestUtil.uid!!,
        onFail = { assert(false) })

    suspend fun assertAllDataIsEmpty() {
        withContext(Dispatchers.Default) {
            delay(1000)
            assert(allData.isEmpty())
        }
    }

    fun assertAllDataSize(size: Int) = assert(allData.size == size)

    fun removeAllData() =
        FirebaseDatabase.getInstance().reference.child(userRepositoryTestUtil.uid!!).removeValue()
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T : BaseModel, U : BaseDataRepository<T>> TestScope.repositoryCrudTest(
    backgroundScope: CoroutineScope,
    dataRepositoryTestUtil: DataRepositoryTestUtil<T, U>,
    data: T,
    addAssert: (T) -> Unit,
    editData: (oldData: T) -> T,
    editAssert: (old: T, new: T) -> Unit,
    loadAssert: (origin: T, loaded: T) -> Unit,
) {
    val userRepositoryTestUtil = dataRepositoryTestUtil.userRepositoryTestUtil
    userRepositoryTestUtil.signOut()

    dataRepositoryTestUtil.allDataCollect(backgroundScope = backgroundScope)
    advanceUntilIdle()
    userRepositoryTestUtil.assertSignOut() // not sign in
    dataRepositoryTestUtil.assertAllDataIsEmpty() // not loaded

    userRepositoryTestUtil.signIn()
    advanceUntilIdle()
    userRepositoryTestUtil.assertSignIn() // sign in
    dataRepositoryTestUtil.assertAllDataIsEmpty() // loaded but data is empty

    dataRepositoryTestUtil.add(data = data) // data add
    advanceUntilIdle()
    dataRepositoryTestUtil.assertAllDataSize(1) // data size is 1

    val addedData = dataRepositoryTestUtil.allData.first()
    addAssert(addedData)

    dataRepositoryTestUtil.edit(oldData = addedData, newData = editData(addedData))
    advanceUntilIdle()
    val editedData = dataRepositoryTestUtil.allData.first()
    editAssert(addedData, editedData)

    userRepositoryTestUtil.signOut()
    advanceUntilIdle()
    userRepositoryTestUtil.assertSignOut() // sign out
    dataRepositoryTestUtil.assertAllDataIsEmpty() // data init

    userRepositoryTestUtil.signIn()
    advanceUntilIdle()
    userRepositoryTestUtil.assertSignIn() // sign in

    withContext(Dispatchers.Default) {
        delay(1000)
        dataRepositoryTestUtil.assertAllDataSize(1) // data loaded
        loadAssert(editedData, dataRepositoryTestUtil.allData.first()) // assert same
    }

    dataRepositoryTestUtil.removeAllData()
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T : BaseContainerModel, U : BaseDataRepository<T>> TestScope.sortTest(
    dataRepositoryTestUtil: DataRepositoryTestUtil<T, U>,
    preferencesRepository: SortPreferenceRepository,
    data: List<T>,
) {
    val userRepositoryTestUtil = dataRepositoryTestUtil.userRepositoryTestUtil

    suspend fun sortInit() {
        preferencesRepository.changeSort(Sort.NAME)
        preferencesRepository.changeOrder(Order.ASCENDING)
    }

    sortInit()

    dataRepositoryTestUtil.allDataCollect(backgroundScope)
    advanceUntilIdle()
    userRepositoryTestUtil.assertSignOut() // not sign in
    dataRepositoryTestUtil.assertAllDataIsEmpty() // no loaded

    userRepositoryTestUtil.signIn()
    advanceUntilIdle()
    userRepositoryTestUtil.assertSignIn() // sign in
    dataRepositoryTestUtil.assertAllDataIsEmpty() // loaded but data is empty

    data.forEach { dataRepositoryTestUtil.add(it) } // add data list
    advanceUntilIdle()
    dataRepositoryTestUtil.assertAllDataSize(9) // data list added

    fun assertSort(firstItemName: String, lastItemName: String) {
        assert(dataRepositoryTestUtil.allData.first().name == firstItemName)
        assert(dataRepositoryTestUtil.allData.last().name == lastItemName)
    }

    assertSort(firstItemName = "0", lastItemName = "8") // Sort: Name, Order: Ascending

    preferencesRepository.changeOrder(Order.DESCENDING)
    advanceUntilIdle()
    assertSort(firstItemName = "8", lastItemName = "0") // Sort: Name, Order: Descending

    preferencesRepository.changeOrder(Order.ASCENDING)
    advanceUntilIdle()
    assertSort(firstItemName = "0", lastItemName = "8") // Sort: Name, Order: Ascending

    preferencesRepository.changeSort(Sort.CREATE)
    advanceUntilIdle()
    assertSort(firstItemName = "0", lastItemName = "8") // Sort: CREATE, Order: Ascending

    preferencesRepository.changeOrder(Order.DESCENDING)
    advanceUntilIdle()
    assertSort(firstItemName = "8", lastItemName = "0") // Sort: CREATE, Order: Descending

    preferencesRepository.changeOrder(Order.ASCENDING)
    advanceUntilIdle()
    assertSort(firstItemName = "0", lastItemName = "8") // Sort: CREATE, Order: Ascending

    preferencesRepository.changeSort(Sort.EDIT)
    advanceUntilIdle()
    assertSort(firstItemName = "0", lastItemName = "8") // Sort: EDIT, Order: Ascending

    preferencesRepository.changeOrder(Order.DESCENDING)
    advanceUntilIdle()
    assertSort(firstItemName = "8", lastItemName = "0")  // Sort: EDIT, Order: Descending

    preferencesRepository.changeOrder(Order.ASCENDING)
    advanceUntilIdle()
    assertSort(firstItemName = "0", lastItemName = "8")  // Sort: EDIT, Order: Ascending

    sortInit() // init

    dataRepositoryTestUtil.removeAllData()
}

suspend fun <T : BaseContainerModel, U : BaseDataRepository<T>> TestScope.addContainerUseCaseTest(
    dataRepositoryTestUtil: DataRepositoryTestUtil<T, U>,
    add: suspend (name: String) -> Unit,
) = addUseCaseTest(
    dataRepositoryTestUtil = dataRepositoryTestUtil,
    add = add,
    addAssert = { addedData, text ->
        assert(addedData.name == text)
    },
    addListAssert = { addedDataList, textList ->
        assert(addedDataList.map { it.name } == textList)
    }
)

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T : BaseModel, U : BaseDataRepository<T>> TestScope.addUseCaseTest(
    dataRepositoryTestUtil: DataRepositoryTestUtil<T, U>,
    add: suspend (text: String) -> Unit,
    addAssert: (addedData: T, text: String) -> Unit,
    addListAssert: (addedDataList: List<T>, textList: List<String>) -> Unit,
) {
    val userRepositoryTestUtil = dataRepositoryTestUtil.userRepositoryTestUtil
    userRepositoryTestUtil.signOut()

    dataRepositoryTestUtil.allDataCollect(backgroundScope)
    advanceUntilIdle()
    userRepositoryTestUtil.assertSignOut() // not sign in
    dataRepositoryTestUtil.assertAllDataIsEmpty() // not loaded

    userRepositoryTestUtil.signIn()
    advanceUntilIdle()
    userRepositoryTestUtil.assertSignIn() // sign in
    dataRepositoryTestUtil.assertAllDataIsEmpty() // loaded but data is empty

    val text1 = "data1"
    add(text1)
    advanceUntilIdle()
    dataRepositoryTestUtil.assertAllDataSize(1)
    addAssert(dataRepositoryTestUtil.allData.first(), text1)

    val text2 = "data2"
    add(text2)
    advanceUntilIdle()
    dataRepositoryTestUtil.assertAllDataSize(2)
    addListAssert(dataRepositoryTestUtil.allData, listOf(text1, text2))

    dataRepositoryTestUtil.removeAllData()
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T : BaseContainerModel, U : BaseDataRepository<T>> TestScope.editContainerUseCaseTest(
    dataRepositoryTestUtil: DataRepositoryTestUtil<T, U>,
    addData: T,
    edit: suspend (oldData: T, name: String) -> Unit,
) {
    val editName = "edit test"

    editUseCaseTest(
        dataRepositoryTestUtil,
        addData = addData,
        addAssert = { addedDta ->
            assert(addedDta.name == addedDta.name)
        },
        edit = { edit(it, editName) },
        editAssert = { newData ->
            assert(newData.name == editName)
        }
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T : BaseModel, U : BaseDataRepository<T>> TestScope.editUseCaseTest(
    dataRepositoryTestUtil: DataRepositoryTestUtil<T, U>,
    addData: T,
    addAssert: (addedDta: T) -> Unit,
    edit: suspend (oldData: T) -> Unit,
    editAssert: (newData: T) -> Unit,
) {
    val userRepositoryTestUtil = dataRepositoryTestUtil.userRepositoryTestUtil
    userRepositoryTestUtil.signOut()

    dataRepositoryTestUtil.allDataCollect(backgroundScope)
    advanceUntilIdle()
    userRepositoryTestUtil.assertSignOut() // not sign in
    dataRepositoryTestUtil.assertAllDataIsEmpty() // not loaded

    userRepositoryTestUtil.signIn()
    advanceUntilIdle()
    userRepositoryTestUtil.assertSignIn()
    dataRepositoryTestUtil.assertAllDataIsEmpty() // loaded but data is empty

    dataRepositoryTestUtil.add(addData)
    advanceUntilIdle()
    dataRepositoryTestUtil.assertAllDataSize(1)

    val oldData = dataRepositoryTestUtil.allData.first()
    addAssert(oldData)

    edit(oldData)
    advanceUntilIdle()
    dataRepositoryTestUtil.assertAllDataSize(1)
    editAssert(dataRepositoryTestUtil.allData.first())

    dataRepositoryTestUtil.removeAllData()
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T : BaseModel, U : BaseDataRepository<T>> TestScope.getAllDataUseCaseTest(
    dataRepositoryTestUtil: DataRepositoryTestUtil<T, U>,
    initDataList: List<T>,
    allDataFlow: StateFlow<List<T>>,
    assertLoadedData: (initDataList: List<T>, loadedDataList: List<T>) -> Unit,
) {
    val userRepositoryTestUtil = dataRepositoryTestUtil.userRepositoryTestUtil
    userRepositoryTestUtil.signOut()

    dataRepositoryTestUtil.allDataCollect(backgroundScope)

    userRepositoryTestUtil.assertSignOut() // not sign in
    dataRepositoryTestUtil.assertAllDataIsEmpty() // not loaded

    userRepositoryTestUtil.signIn()
    advanceUntilIdle()
    userRepositoryTestUtil.assertSignIn() // sign in
    dataRepositoryTestUtil.assertAllDataIsEmpty() // loaded but data is empty

    initDataList.forEach { dataRepositoryTestUtil.add(it) }
    advanceUntilIdle()

    assert(allDataFlow.value.size == initDataList.size)

    assertLoadedData(initDataList, allDataFlow.value)

    userRepositoryTestUtil.signOut()
    advanceUntilIdle()
    userRepositoryTestUtil.assertSignOut() // sign out
    dataRepositoryTestUtil.assertAllDataIsEmpty() // not loaded

    userRepositoryTestUtil.signIn()
    advanceUntilIdle()
    userRepositoryTestUtil.assertSignIn() // sign out
    withContext(Dispatchers.Default) {
        delay(1000)
        dataRepositoryTestUtil.assertAllDataSize(initDataList.size)
    }
    assertLoadedData(initDataList, allDataFlow.value)

    dataRepositoryTestUtil.removeAllData()
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T : BaseModel, U : BaseDataRepository<T>> TestScope.dataLoadingFlowTest(
    dataRepositoryTestUtil: DataRepositoryTestUtil<T, U>,
    initDataList: List<T>,
    loadingFlow: StateFlow<Boolean>,
) {
    val userRepositoryTestUtil = dataRepositoryTestUtil.userRepositoryTestUtil
    userRepositoryTestUtil.signIn()
    advanceUntilIdle()
    initDataList.forEach { dataRepositoryTestUtil.add(it) }
    advanceUntilIdle()

    backgroundScope.launch(dataRepositoryTestUtil.dispatcher) { loadingFlow.collectLatest { } }
    assert(loadingFlow.value)

    dataRepositoryTestUtil.load()
    assert(loadingFlow.value)
    advanceUntilIdle()
    assert(!loadingFlow.value)

    dataRepositoryTestUtil.allDataCollect(backgroundScope)

    userRepositoryTestUtil.signOut()
    advanceUntilIdle()
    userRepositoryTestUtil.assertSignOut()
    dataRepositoryTestUtil.assertAllDataIsEmpty()

    userRepositoryTestUtil.signIn()
    assert(loadingFlow.value)
    advanceUntilIdle()
    withContext(Dispatchers.Default) {
        delay(1000)
        assert(!loadingFlow.value)
    }

    dataRepositoryTestUtil.removeAllData()
}