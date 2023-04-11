package com.leebeebeom.clothinghelper.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.data.waitTime
import com.leebeebeom.clothinghelper.domain.model.BaseModel
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
suspend inline fun <T : BaseModel> TestScope.repositoryOrderTest(
    repository: BaseDataRepository<T>,
    userRepository: UserRepository,
    initDataList: List<T>,
    assertOrder: (origin: List<T>, new: List<T>) -> Unit
) {
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { repository.allDataFlow.collect() }
    waitTime()

    initDataList.shuffled().map { coroutineScope { async { repository.add(it) } } }.awaitAll()
    waitTime()
    assertOrder(initDataList, repository.allDataFlow.first().data)

    Firebase.database.reference.child(userRepository.userFlow.first()!!.uid).removeValue().await()
}