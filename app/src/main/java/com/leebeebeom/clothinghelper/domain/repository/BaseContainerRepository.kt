package com.leebeebeom.clothinghelper.domain.repository

import com.leebeebeom.clothinghelper.domain.model.BaseDatabaseContainerModel
import kotlinx.coroutines.flow.StateFlow

interface BaseContainerRepository<T : BaseDatabaseContainerModel> : BaseDataRepository<T> {
    val allSortedData: StateFlow<List<T>>
}