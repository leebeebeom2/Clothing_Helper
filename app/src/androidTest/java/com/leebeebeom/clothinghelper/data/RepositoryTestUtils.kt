package com.leebeebeom.clothinghelper.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.leebeebeom.clothinghelper.domain.model.BaseModel
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
fun <T : BaseModel, U : BaseDataRepository<T>> repositoryCrudTest(
    type: Class<T>,
    data: T,
    repository: U,
    uid: String,
    addAssert: (T) -> Unit,
    newData: (origin: T) -> T,
    editAssert: (origin: T, new: T) -> Unit,
) = runTest {
    signIn()

    val dispatcher = UnconfinedTestDispatcher(testScheduler)

    repository.getAllData(dispatcher = dispatcher, uid = uid, type = type) {
        assert(false)
    }

    repository.add(dispatcher = dispatcher, data = data, uid = uid) {
        assert(false)
    }

    val addedData =
        repository.getAllData(dispatcher = dispatcher, uid = uid, type = type) { assert(false) }
            .first().first()
    addAssert(addedData)

    repository.edit(dispatcher = dispatcher, newData = newData(addedData), uid = uid) {
        assert(false)
    }

    val editData =
        repository.getAllData(dispatcher = dispatcher, uid = uid, type = type) { assert(false) }
            .first().first()
    editAssert(addedData, editData)

    FirebaseDatabase.getInstance().reference.child(uid).removeValue()
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <T : BaseModel, U : BaseDataRepository<T>> repositorySignOutTest(
    type: Class<T>,
    data1: T,
    data2: T,
    repository: U,
    uid: String,
) = runTest {
    val dispatcher = UnconfinedTestDispatcher(testScheduler)

    signIn()

    val allDataFlow =
        repository.getAllData(dispatcher = dispatcher, uid = uid, type = type) { assert(false) }

    repository.add(dispatcher = dispatcher, data = data1, uid = uid) { assert(false) }
    repository.add(dispatcher = dispatcher, data = data2, uid = uid) { assert(false) }

    assert(allDataFlow.first().size == 2)

    FirebaseDatabase.getInstance().reference.child(uid).removeValue()
}

private fun signIn() = FirebaseAuth.getInstance().signInWithEmailAndPassword("1@a.com", "111111")