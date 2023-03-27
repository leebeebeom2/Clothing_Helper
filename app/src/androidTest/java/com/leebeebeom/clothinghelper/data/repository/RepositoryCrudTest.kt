package com.leebeebeom.clothinghelper.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.data.SignInPassword
import com.leebeebeom.clothinghelper.data.waitTime
import com.leebeebeom.clothinghelper.domain.model.BaseModel
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import com.leebeebeom.clothinghelper.domain.repository.DataResult
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
suspend inline fun <T : BaseModel> TestScope.repositoryCrudTest(
    userRepository: UserRepository,
    repository: BaseDataRepository<T>,
    initialSize: Int,
    refPath: DataBasePath,
    data: T,
    addAssert: (T) -> Unit,
    editData: (oldData: T) -> T,
    editAssert: (old: T, new: T) -> Unit,
) {
    userRepository.signIn(email = RepositoryTestEmail, password = SignInPassword)
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { repository.allDataFlow.collect() }

    val loadTestData = repository.allDataFlow.first()
    assert(loadTestData is DataResult.Success)
    assert(loadTestData.data.size == initialSize)

    repository.add(data)
    waitTime()
    val addTestData = repository.allDataFlow.first()
    assert(addTestData is DataResult.Success)
    assert(addTestData.data.size == initialSize + 1)

    val addedData = addTestData.data.last()
    addAssert(addedData)

    repository.push(data = editData(addedData))
    waitTime()
    val editedData = repository.allDataFlow.first().data.last()
    editAssert(addedData, editedData)

    userRepository.signOut()
    waitTime()
    assert(repository.allDataFlow.first().data.isEmpty())

    userRepository.signIn(email = RepositoryTestEmail, password = SignInPassword)
    waitTime()
    assert(repository.allDataFlow.first().data.size == initialSize + 1)
    assert(editedData == repository.allDataFlow.first().data.last())

    Firebase.database.reference.child(userRepository.userFlow.first()!!.uid)
        .child(refPath.path)
        .child(editedData.key).removeValue().await()

    userRepository.signOut()
}