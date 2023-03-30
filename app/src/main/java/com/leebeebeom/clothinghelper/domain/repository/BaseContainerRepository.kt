package com.leebeebeom.clothinghelper.domain.repository

import com.leebeebeom.clothinghelper.domain.model.BaseContainerModel
import kotlinx.coroutines.flow.SharedFlow

interface BaseContainerRepository<T : BaseContainerModel> : BaseDataRepository<T> {
    val allDataNames: SharedFlow<List<String>>
}