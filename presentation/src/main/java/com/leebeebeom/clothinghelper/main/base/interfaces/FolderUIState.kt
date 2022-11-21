package com.leebeebeom.clothinghelper.main.base.interfaces

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.toStable
import com.leebeebeom.clothinghelperdomain.model.container.Folder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

interface FolderUIState {
    val allFolders: List<StableFolder>

    fun getFolders(parentKey: String): ImmutableList<StableFolder>
    fun updateAllFolders(folders: List<Folder>)
}

class FolderUIStateImpl : FolderUIState {
    override var allFolders by mutableStateOf(emptyList<StableFolder>())
        private set

    override fun getFolders(parentKey: String): ImmutableList<StableFolder> =
        derivedStateOf {
            allFolders.filter { it.parentKey == parentKey }.toImmutableList()
        }.value

    override fun updateAllFolders(folders: List<Folder>) {
        allFolders = folders.map { it.toStable() }
    }
}
