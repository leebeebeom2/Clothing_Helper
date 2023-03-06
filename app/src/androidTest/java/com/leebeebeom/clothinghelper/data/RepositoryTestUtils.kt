@file:OptIn(ExperimentalCoroutinesApi::class)

package com.leebeebeom.clothinghelper.data

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.leebeebeom.clothinghelper.data.repository.preference.Order
import com.leebeebeom.clothinghelper.data.repository.preference.Sort
import com.leebeebeom.clothinghelper.domain.model.BaseContainerModel
import com.leebeebeom.clothinghelper.domain.model.BaseModel
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
    backgroundScope.launch(dispatcher) {
        userRepository.user.collectLatest {
            repository.load(dispatcher = dispatcher, uid = it?.uid, onFail = { })
        }
    }

    backgroundScope.launch(dispatcher) { repository.allData.collectLatest { } }

    userRepository.signIn(
        email = "1@a.com",
        password = "111111",
        dispatcher = dispatcher,
        firebaseResult = successResult
    )
    advanceUntilIdle()
    assert(userRepository.user.value != null)
    assert(repository.allData.value.isEmpty())

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

    FirebaseDatabase.getInstance().reference.child(userRepository.user.value!!.uid)
        .child(refPath).removeValue()
}