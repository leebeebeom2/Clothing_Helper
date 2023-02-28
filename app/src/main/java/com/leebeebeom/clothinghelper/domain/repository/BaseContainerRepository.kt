package com.leebeebeom.clothinghelper.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface BaseContainerRepository<T> : BaseDataRepository<T> {
    val allSortedData: StateFlow<List<T>>
}