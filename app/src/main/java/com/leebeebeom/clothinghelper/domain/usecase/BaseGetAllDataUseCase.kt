package com.leebeebeom.clothinghelper.domain.usecase

import com.leebeebeom.clothinghelper.domain.model.BaseModel
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow

abstract class BaseGetAllDataUseCase<T : BaseModel>(
    private val repository: BaseDataRepository<T>,
    private val appScope: CoroutineScope,
) {
    private lateinit var allDataFlow: StateFlow<List<T>>

    suspend fun getAllData(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        uid: String,
        onFail: (Exception) -> Unit,
    ) = repository.getAllData(dispatcher = dispatcher, uid = uid, onFail = onFail)
}