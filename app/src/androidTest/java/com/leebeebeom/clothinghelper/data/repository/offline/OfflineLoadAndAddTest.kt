package com.leebeebeom.clothinghelper.data.repository.offline

import com.leebeebeom.clothinghelper.data.waitTime
import com.leebeebeom.clothinghelper.domain.model.BaseModel
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
suspend inline fun <T : BaseModel> TestScope.offlineLoadAndAddTest(
    repository: BaseDataRepository<T>,
    addDataList: Pair<T, T>,
    initialSize: Int,
) {
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { repository.allDataStream.collect() }
    waitTime()
    assert(repository.allDataStream.first().data.size == initialSize)

    repository.add(addDataList.first)
    waitTime()

    assert(repository.allDataStream.first().data.size == initialSize + 1)

    repository.add(addDataList.second)
    waitTime()

    assert(repository.allDataStream.first().data.size == initialSize + 2)
}