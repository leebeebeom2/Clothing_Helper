package com.leebeebeom.clothinghelper.domain.repository

import com.leebeebeom.clothinghelper.domain.model.Folder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.coroutines.flow.SharedFlow

interface FolderRepository : BaseDataRepository<Folder> {
    val allFoldersMapFlow: SharedFlow<FolderResultMap>
    val folderNamesMapFlow: SharedFlow<ImmutableMap<String, ImmutableSet<String>>>
    val foldersSizeMapFlow: SharedFlow<ImmutableMap<String, Int>>
}

sealed class FolderResultMap(val data: ImmutableMap<String, ImmutableList<Folder>>) {
    class Success(data: ImmutableMap<String, ImmutableList<Folder>>) : FolderResultMap(data)
    class Fail(data: ImmutableMap<String, ImmutableList<Folder>>, val exception: Throwable) :
        FolderResultMap(data)
}