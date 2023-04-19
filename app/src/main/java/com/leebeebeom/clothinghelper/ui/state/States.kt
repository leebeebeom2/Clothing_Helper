package com.leebeebeom.clothinghelper.ui.state

import com.leebeebeom.clothinghelper.domain.model.Folder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf

interface ToastUiState {
    val toastTexts: ImmutableList<Int>
}

interface LoadingUiState {
    val isLoading: Boolean
}

interface FoldersState {
    val foldersMap: ImmutableMap<String, ImmutableList<Folder>>

    fun getFolders(key: String) =
        foldersMap.getOrDefault(key = key, defaultValue = persistentListOf())
}

interface FolderNamesState {
    val folderNamesMap: ImmutableMap<String, ImmutableSet<String>>

    fun getFolderNames(key: String) =
        folderNamesMap.getOrDefault(key = key, defaultValue = persistentSetOf())
}

interface FoldersSizeState {
    val foldersSizeMap: ImmutableMap<String, Int>

    fun getFoldersSize(key: String) = foldersSizeMap.getOrDefault(key = key, defaultValue = 0)
}