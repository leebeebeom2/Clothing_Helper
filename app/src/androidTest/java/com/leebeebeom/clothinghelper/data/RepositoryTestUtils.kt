package com.leebeebeom.clothinghelper.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.leebeebeom.clothinghelper.domain.model.BaseDatabaseModel
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
fun <T : BaseDatabaseModel, U : BaseDataRepository<T>> repositoryCrudTest(
    data: T,
    repository: U,
    uid: String,
    type: Class<T>,
    addAssert: (T) -> Unit,
    newData: (origin: T) -> T,
    editAssert: (origin: T, new: T) -> Unit,
) = runTest {
    signIn()

    val dispatcher = UnconfinedTestDispatcher(testScheduler)

    repository.load(dispatcher = dispatcher, uid = uid, type = type) {
        assert(false)
    }

    repository.add(dispatcher = dispatcher, data = data, uid = uid) {
        assert(false)
    }

    val addedData = repository.allData.first().first()
    addAssert(addedData)

    repository.edit(dispatcher = dispatcher, newData = newData(addedData), uid = uid) {
        assert(false)
    }

    val editData = repository.allData.first().first()
    editAssert(addedData, editData)

    FirebaseDatabase.getInstance().reference.child(uid).removeValue()
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <T : BaseDatabaseModel, U : BaseDataRepository<T>> repositorySignOutTest(
    data1: T,
    data2: T,
    repository: U,
    uid: String,
    type: Class<T>,
) = runTest {
    val dispatcher = UnconfinedTestDispatcher(testScheduler)

    signIn()

    repository.load(dispatcher = dispatcher, uid = uid, type = type) { assert(false) }

    repository.add(dispatcher = dispatcher, data = data1, uid = uid) { assert(false) }
    repository.add(dispatcher = dispatcher, data = data2, uid = uid) { assert(false) }

    assert(repository.allData.value.size == 2)

    repository.load(dispatcher = dispatcher, uid = null, type = type) { assert(false) }

    FirebaseDatabase.getInstance().reference.child(uid).removeValue()
}

private fun signIn() = FirebaseAuth.getInstance().signInWithEmailAndPassword("1@a.com", "111111")