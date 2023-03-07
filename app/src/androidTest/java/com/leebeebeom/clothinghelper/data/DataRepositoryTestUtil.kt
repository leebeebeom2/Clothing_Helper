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
) {
    val userRepositoryTestUtil = UserRepositoryTestUtil(repositoryProvider = repositoryProvider)
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

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T : BaseContainerModel, U : BaseDataRepository<T>> TestScope.addUseCaseTest(
    dataRepositoryTestUtil: DataRepositoryTestUtil<T, U>,
    add: suspend (name: String) -> Unit,
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

    val name1 = "data1"
    add(name1)
    advanceUntilIdle()
    dataRepositoryTestUtil.assertAllDataSize(1)
    assert(dataRepositoryTestUtil.allData.first().name == name1)

    val name2 = "data2"
    add(name2)
    advanceUntilIdle()
    dataRepositoryTestUtil.assertAllDataSize(2)
    assert(dataRepositoryTestUtil.allData.map { it.name } == listOf(name1, name2))

    dataRepositoryTestUtil.removeAllData()
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T : BaseContainerModel, U : BaseDataRepository<T>> TestScope.editUseCaseTest(
    dataRepositoryTestUtil: DataRepositoryTestUtil<T, U>,
    addData: T,
    edit: suspend (oldData: T, name: String) -> Unit,
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
    assert(dataRepositoryTestUtil.allData.first().name == addData.name)

    val oldData = dataRepositoryTestUtil.allData.first()
    val newName = "edited data"
    edit(oldData, newName)
    advanceUntilIdle()
    dataRepositoryTestUtil.assertAllDataSize(1)
    assert(dataRepositoryTestUtil.allData.first().name == newName)

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
        dataRepositoryTestUtil.assertAllDataSize(10) // not loaded
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