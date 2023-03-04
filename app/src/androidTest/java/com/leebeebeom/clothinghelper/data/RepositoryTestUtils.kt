package com.leebeebeom.clothinghelper.data

import com.google.firebase.database.FirebaseDatabase
import com.leebeebeom.clothinghelper.data.repository.FirebaseResult
import com.leebeebeom.clothinghelper.domain.model.BaseModel
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
fun <T : BaseModel, U : BaseDataRepository<T>> repositoryCrudTest(
    userRepository: UserRepository,
    data: T,
    repository: U,
    addAssert: (T) -> Unit,
    newData: (origin: T) -> T,
    editAssert: (origin: T, new: T) -> Unit,
) = runTest {
    val dispatcher = StandardTestDispatcher(testScheduler)

    userRepository.signOut()

    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
        userRepository.user.collectLatest { }
    }

    userRepository.signIn(
        "1@a.com",
        "111111",
        dispatcher = dispatcher,
        firebaseResult = object : FirebaseResult {
            override fun success() = assert(true)

            override fun fail(exception: Exception) = assert(false)
        })

    withContext(Dispatchers.Default) {
        delay(1000)
        assert(userRepository.user.value != null)
    }

    val allDataFlow = repository.getAllData(
        dispatcher = dispatcher,
        uid = userRepository.user.value!!.uid,
        onFail = { assert(false) })

    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
        allDataFlow.collectLatest {}
    }

    repository.add(dispatcher = dispatcher,
        data = data,
        uid = userRepository.user.value!!.uid,
        onFail = { assert(false) })

    val addedData: T

    withContext(Dispatchers.Default) {
        delay(1000)
        addedData = allDataFlow.value.first()
        addAssert(addedData)
    }

    repository.edit(dispatcher = dispatcher,
        newData = newData(addedData),
        uid = userRepository.user.value!!.uid,
        onFail = { assert(false) })

    withContext(Dispatchers.Default) {
        delay(1000)
        val editData = allDataFlow.value.first()
        editAssert(addedData, editData)
    }

    val signOutData = repository.getAllData(dispatcher, null, onFail = { assert(false) }) // signOut

    backgroundScope.launch {
        signOutData.collectLatest { assert(it.isEmpty()) }
    }

    val allData = repository.getAllData(dispatcher,
        userRepository.user.value!!.uid,
        onFail = { assert(false) })

    backgroundScope.launch {
        allData.collectLatest { assert(it.size == 2) }
    }

    FirebaseDatabase.getInstance().reference.child(userRepository.user.value!!.uid).removeValue()
}