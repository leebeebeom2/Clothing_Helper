package com.leebeebeom.clothinghelper.domain.repository

import com.leebeebeom.clothinghelper.domain.model.Folder
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.coroutines.flow.SharedFlow

interface FolderRepository : BaseContainerRepository<Folder> {
    val folderNamesMapFlow: SharedFlow<ImmutableMap<String, ImmutableSet<String>>>
    val folderSizeMapFlow: SharedFlow<ImmutableMap<String, Int>>
}