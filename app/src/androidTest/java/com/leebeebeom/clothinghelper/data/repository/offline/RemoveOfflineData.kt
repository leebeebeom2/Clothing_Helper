package com.leebeebeom.clothinghelper.data.repository.offline

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.data.repository.DataBasePath
import com.leebeebeom.clothinghelper.data.waitTime
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun TestScope.removeOfflineData(
    repository: BaseDataRepository<*>,
    userRepository: UserRepository,
    initialSize: Int,
    databasePath: DataBasePath,
) {
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { repository.allDataFlow.collect() }

    val lastDataList = repository.allDataFlow.first().data.drop(initialSize)
    assert(lastDataList.size == 2)

    val ref = Firebase.database.reference.child(userRepository.userFlow.first()!!.uid)
        .child(databasePath.path)
    ref.child(lastDataList.first().key).removeValue()
    ref.child(lastDataList.last().key).removeValue()

    waitTime(1000)
}