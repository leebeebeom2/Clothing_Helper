package com.leebeebeom.clothinghelper.domain.usecase

import com.leebeebeom.clothinghelper.domain.model.BaseModel
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

abstract class BaseGetAllDataUseCase<T : BaseModel>(private val repository: BaseDataRepository<T>) {
    suspend fun getAllData(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        uid: String,
        onFail: (Exception) -> Unit,
    ) = repository.getAllData(dispatcher = dispatcher, uid = uid, onFail = onFail)
}