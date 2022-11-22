package com.leebeebeom.clothinghelper.main.base.interfaces

import androidx.compose.runtime.*
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.toStable
import com.leebeebeom.clothinghelperdomain.model.container.Folder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

interface FolderUIState {
    val allFolders: List<StableFolder>

    fun getFolders(parentKey: String): ImmutableList<StableFolder>
    fun loadAllFolders(folders: List<Folder>)
}

class FolderUIStateImpl : FolderUIState {
    override var allFolders by mutableStateOf(emptyList<StableFolder>())
        private set

    private val folderMap = hashMapOf<String, State<ImmutableList<StableFolder>>>()

    override fun getFolders(parentKey: String): ImmutableList<StableFolder> {
        if (folderMap[parentKey] == null) {
            folderMap[parentKey] = derivedStateOf {
                allFolders.filter { it.parentKey == parentKey }.toImmutableList()
            }
        }
        return folderMap[parentKey]?.value!!
    }

    override fun loadAllFolders(folders: List<Folder>) {
        allFolders = folders.map { it.toStable() }
    }
}
