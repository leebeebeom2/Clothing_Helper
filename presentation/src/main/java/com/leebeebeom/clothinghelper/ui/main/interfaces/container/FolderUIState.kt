package com.leebeebeom.clothinghelper.ui.main.interfaces.container

import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.toStable
import com.leebeebeom.clothinghelperdomain.model.data.Folder
import kotlinx.collections.immutable.ImmutableList

interface FolderUIState {
    val allFolders: List<StableFolder>

    fun loadAllFolders(allFolders: List<Folder>)
    fun getFolders(parentKey: String): ImmutableList<StableFolder>
}

class FolderUIStateImpl : FolderUIState, ContainerUIState<StableFolder> by ContainerUIStateImpl() {
    override val allFolders get() = allItems

    override fun loadAllFolders(allFolders: List<Folder>) = load(allFolders.map { it.toStable() })
    override fun getFolders(parentKey: String) = getItems(parentKey) { it.parentKey == parentKey }
}
