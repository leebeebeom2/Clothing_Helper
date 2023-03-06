@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalCoroutinesApi::class)

package com.leebeebeom.clothinghelper.data

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.leebeebeom.clothinghelper.data.repository.preference.Order
import com.leebeebeom.clothinghelper.data.repository.preference.Sort
import com.leebeebeom.clothinghelper.domain.model.BaseContainerModel
import com.leebeebeom.clothinghelper.domain.model.BaseModel
import com.leebeebeom.clothinghelper.domain.model.User
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.*

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T : BaseModel, U : BaseDataRepository<T>> TestScope.repositoryCrudTest(
    dispatcher: TestDispatcher,
    userRepository: UserRepository,
    data: T,
    repository: U,
    addAssert: (T) -> Unit,
    newData: (origin: T) -> T,
    editAssert: (origin: T, new: T) -> Unit,
) {
    allDataCollect(
        dispatcher = dispatcher, userRepository = userRepository, repository = repository
    )

    signIn(dispatcher = dispatcher, userRepository = userRepository)

    assert(userRepository.user.value != null) // signIn
    assert(repository.allData.value.isEmpty()) // initValue

    repository.add(dispatcher = dispatcher,
        data = data,
        uid = userRepository.user.value!!.uid,
        onFail = { assert(false) })
    advanceUntilIdle()
    assert(repository.allData.value.size == 1)

    val addedData = repository.allData.value.first()
    addAssert(addedData)

    repository.edit(dispatcher = dispatcher,
        oldData = addedData,
        newData = newData(addedData),
        uid = userRepository.user.value!!.uid,
        onFail = { assert(false) })
    advanceUntilIdle()

    val editData = repository.allData.value.first()
    editAssert(addedData, editData)

    userRepository.signOut()
    advanceUntilIdle()

    withContext(Dispatchers.Default) {
        delay(1000)
        assert(repository.allData.value.isEmpty())
    }

    userRepository.signIn(
        email = "1@a.com",
        password = "111111",
        dispatcher = dispatcher,
        firebaseResult = successResult
    )
    advanceUntilIdle()
    withContext(Dispatchers.Default) {
        delay(1000)
        assert(repository.allData.value.size == 1)
    }

    FirebaseDatabase.getInstance().reference.child(userRepository.user.value!!.uid).removeValue()
}

suspend fun <T : BaseContainerModel, U : BaseDataRepository<T>> TestScope.sortTest(
    dispatcher: TestDispatcher,
    preferencesRepository: SortPreferenceRepository,
    userRepository: UserRepository,
    repository: U,
    data: List<T>,
    refPath: String,
) {
    preferencesRepository.changeSort(Sort.NAME)
    preferencesRepository.changeOrder(Order.ASCENDING)

    backgroundScope.launch {
        userRepository.user.collect {
            repository.load(
                dispatcher = dispatcher, uid = it?.uid
            ) {}
        }
    }
    backgroundScope.launch { repository.allData.collect {} }

    userRepository.signIn(
        "1@a.com", "111111", firebaseResult = successResult, dispatcher = dispatcher
    )
    advanceUntilIdle()

    data.forEach {
        repository.add(
            dispatcher = dispatcher, data = it, uid = userRepository.user.value!!.uid
        ) { assert(false) }
    }
    advanceUntilIdle()
    assert(repository.allData.value.size == 9)

    Log.d("TAG", "sortTest: ${repository.allData.value.first().name}")
    assert(repository.allData.value.first().name == "0")
    assert(repository.allData.value.last().name == "8")

    preferencesRepository.changeOrder(Order.DESCENDING)
    advanceUntilIdle()
    assert(repository.allData.value.first().name == "8")
    assert(repository.allData.value.last().name == "0")

    preferencesRepository.changeOrder(Order.ASCENDING)
    advanceUntilIdle()
    assert(repository.allData.value.first().name == "0")
    assert(repository.allData.value.last().name == "8")

    preferencesRepository.changeSort(Sort.CREATE)
    advanceUntilIdle()
    assert(repository.allData.value.first().name == "0")
    assert(repository.allData.value.last().name == "8")

    preferencesRepository.changeOrder(Order.DESCENDING)
    advanceUntilIdle()
    assert(repository.allData.value.first().name == "8")
    assert(repository.allData.value.last().name == "0")

    preferencesRepository.changeOrder(Order.ASCENDING)
    advanceUntilIdle()
    assert(repository.allData.value.first().name == "0")
    assert(repository.allData.value.last().name == "8")

    preferencesRepository.changeSort(Sort.EDIT)
    advanceUntilIdle()
    assert(repository.allData.value.first().name == "0")
    assert(repository.allData.value.last().name == "8")

    preferencesRepository.changeOrder(Order.DESCENDING)
    advanceUntilIdle()
    assert(repository.allData.value.first().name == "8")
    assert(repository.allData.value.last().name == "0")

    preferencesRepository.changeOrder(Order.ASCENDING)
    advanceUntilIdle()
    assert(repository.allData.value.first().name == "0")
    assert(repository.allData.value.last().name == "8")

    FirebaseDatabase.getInstance().reference.child(userRepository.user.value!!.uid).child(refPath)
        .removeValue()
}

suspend fun <T : BaseContainerModel, U : BaseDataRepository<T>> TestScope.addUseCaseTest(
    dispatcher: TestDispatcher,
    userRepository: UserRepository,
    repository: U,
    refPath: String,
    add: suspend (name: String) -> Unit,
) {
    allDataCollect(
        dispatcher = dispatcher, userRepository = userRepository, repository = repository
    )

    userRepository.signIn(
        email = "1@a.com",
        password = "111111",
        firebaseResult = successResult,
        dispatcher = dispatcher
    )
    advanceUntilIdle()
    assert(repository.allData.value.isEmpty())

    val name1 = "sub category"
    add(name1)
    advanceUntilIdle()
    assert(repository.allData.value.size == 1)
    assert(repository.allData.value.first().name == name1)

    val name2 = "sub category2"
    add(name2)
    advanceUntilIdle()
    val allSubCategories = repository.allData.value
    assert(allSubCategories.size == 2)
    assert(allSubCategories.any { it.name == name1 })
    assert(allSubCategories.any { it.name == name2 })

    FirebaseDatabase.getInstance().reference.child(userRepository.user.value!!.uid).child(refPath)
        .removeValue()
}

suspend fun TestScope.userCollect(
    dispatcher: TestDispatcher,
    userRepository: UserRepository,
    collect: suspend (User?) -> Unit = {},
) = backgroundScope.launch(dispatcher) { userRepository.user.collectLatest(action = collect) }

suspend fun <T : BaseModel, U : BaseDataRepository<T>> TestScope.allDataCollect(
    dispatcher: TestDispatcher,
    userRepository: UserRepository,
    repository: U,
    collect: suspend (List<T>) -> Unit = {},
) {
    userCollect(dispatcher = dispatcher, userRepository = userRepository) {
        repository.load(dispatcher = dispatcher, uid = it?.uid, onFail = { assert(false) })
        advanceUntilIdle()
    }
    backgroundScope.launch(dispatcher) { repository.allData.collectLatest(collect) }
}

suspend fun TestScope.signIn(dispatcher: TestDispatcher, userRepository: UserRepository) {
    userRepository.signIn(
        email = "1@a.com",
        password = "111111",
        firebaseResult = successResult,
        dispatcher = dispatcher
    )
    advanceUntilIdle()
}