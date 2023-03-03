package com.leebeebeom.clothinghelper.domain.usecase

import com.leebeebeom.clothinghelper.domain.model.BaseModel
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

abstract class BaseGetAllDataUseCase<T : BaseModel>(
    private val repository: BaseDataRepository<T>,
    private val appScope: CoroutineScope,
) {
    private lateinit var allDataFlow: StateFlow<List<T>>

    suspend fun getAllData(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        type: Class<T>,
        uid: String,
        onFail: (Exception) -> Unit,
    ): StateFlow<List<T>> {
        if (::allDataFlow.isInitialized) return allDataFlow

        allDataFlow =
            repository.getAllData(dispatcher = dispatcher, uid = uid, type = type, onFail = onFail)
                .stateIn(appScope, SharingStarted.WhileSubscribed(5000), emptyList())
        return allDataFlow
    }
}