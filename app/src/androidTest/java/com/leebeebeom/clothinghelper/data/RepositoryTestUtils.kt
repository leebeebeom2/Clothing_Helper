package com.leebeebeom.clothinghelper.data

import com.google.firebase.database.FirebaseDatabase
import com.leebeebeom.clothinghelper.data.repository.FirebaseResult
import com.leebeebeom.clothinghelper.domain.model.BaseModel
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
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

    userRepository.signIn(email = "1@a.com",
        password = "111111",
        dispatcher = dispatcher,
        firebaseResult = object : FirebaseResult {
            override fun success() = assert(true)

            override fun fail(exception: Exception) = assert(false)
        })
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

    userRepository.signIn(email = "1@a.com",
        password = "111111",
        dispatcher = dispatcher,
        firebaseResult = object : FirebaseResult {
            override fun success() = assert(true)

            override fun fail(exception: Exception) = assert(false)
        })
    advanceUntilIdle()
    withContext(Dispatchers.Default) {
        delay(1000)
        assert(repository.allData.value.size == 1)
    }

    FirebaseDatabase.getInstance().reference.child(userRepository.user.value!!.uid).removeValue()
}