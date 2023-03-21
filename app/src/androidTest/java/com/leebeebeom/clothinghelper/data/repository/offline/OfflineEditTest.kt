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
suspend fun <T : BaseModel> TestScope.offlineEditTest(
    repository: BaseDataRepository<T>,
    initialSize: Int,
    getEditData: (T) -> T,
    editAssert: (edit: T, edited: T) -> Unit,
) {
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { repository.allDataStream.collect() }
    waitTime()
    assert(repository.allDataStream.first().data.size == initialSize + 2)

    val lastData = repository.allDataStream.first().data.last()

    val editData = getEditData(lastData)
    repository.push(editData)
    waitTime()

    val editedData = repository.allDataStream.first().data.last()
    editAssert(editData, editedData)
}