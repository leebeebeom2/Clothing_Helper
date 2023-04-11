package com.leebeebeom.clothinghelper.domain.repository

import com.leebeebeom.clothinghelper.domain.model.BaseModel
import com.leebeebeom.clothinghelper.domain.model.Folder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.flow.SharedFlow

interface BaseDataRepository<T : BaseModel> {
    val allDataFlow: SharedFlow<DataResult<T>>
    suspend fun add(data: T)
    suspend fun push(data: T)
}

sealed class DataResult<T>(val data: ImmutableList<T>) {
    class Success<T>(data: ImmutableList<T>) : DataResult<T>(data)
    class Fail<T>(data: ImmutableList<T>, val exception: Throwable) : DataResult<T>(data)
}

inline fun DataResult<Folder>.toFolderResultMap(keySelector: (Folder) -> String): FolderResultMap {
    val map =
        data.groupBy(keySelector).mapValues { mapElement -> mapElement.value.toImmutableList() }
            .toImmutableMap()

    return when (this) {
        is DataResult.Success -> FolderResultMap.Success(map)
        is DataResult.Fail -> FolderResultMap.Fail(map, exception)
    }
}