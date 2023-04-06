package com.leebeebeom.clothinghelper.ui.drawer.component.folder

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerItemsColumn
import com.leebeebeom.clothinghelper.ui.util.*
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun DrawerFolders(
    folders: () -> ImmutableList<Folder>,
    folderNames: () -> ImmutableSet<String>,
    startPadding: Dp,
    backgroundColor: Color,
    foldersMap: () -> FoldersMap,
    folderNamesMap: () -> FolderNamesMap,
    foldersSizeMap: () -> FoldersSizeMap,
    itemSizeMap: () -> ImmutableMap<String, Int>,
    onFolderClick: (Folder) -> Unit,
    addFolder: AddFolder,
    editFolder: EditFolder
) {
    DrawerItemsColumn(modifier = Modifier.background(backgroundColor)) {
        folders().forEach { folder ->
            DrawerFolder(
                folder = folder,
                onFolderClick = onFolderClick,
                startPadding = startPadding,
                backgroundColor = backgroundColor,
                foldersMap = foldersMap,
                folderNames = folderNames,
                folderSizeMap = foldersSizeMap,
                folderNamesMap = folderNamesMap,
                itemSize = itemSizeMap,
                addFolder = addFolder,
                editFolder = editFolder
            )
        }
    }
}