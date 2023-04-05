package com.leebeebeom.clothinghelper.domain.repository

import com.leebeebeom.clothinghelper.domain.model.BaseContainerModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.coroutines.flow.SharedFlow

interface BaseContainerRepository<T : BaseContainerModel, K> : BaseDataRepository<T> {
    val allDataMapFlow: SharedFlow<DataResultMap<K, T>>
    val dataNamesMapFlow: SharedFlow<ImmutableMap<K, ImmutableSet<String>>>
    val dataSizeMapFlow: SharedFlow<ImmutableMap<K, Int>>
}

sealed class DataResultMap<K, T>(val data: ImmutableMap<K, ImmutableList<T>>) {
    class Success<K, T>(data: ImmutableMap<K, ImmutableList<T>>) : DataResultMap<K, T>(data)
    class Fail<K, T>(data: ImmutableMap<K, ImmutableList<T>>, val exception: Throwable) :
        DataResultMap<K, T>(data)
}