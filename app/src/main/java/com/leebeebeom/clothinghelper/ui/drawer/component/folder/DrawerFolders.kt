package com.leebeebeom.clothinghelper.ui.drawer.component.folder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import com.leebeebeom.clothinghelper.ui.util.EditFolder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun DrawerFolders(
    parentKey: String,
    folders: (parentKey: String) -> ImmutableList<Folder>,
    folderNames: (parentKey: String) -> ImmutableSet<String>,
    startPadding: Dp = 12.dp,
    backgroundColor: Color,
    foldersSize: (parentKey: String) -> Int,
    itemSize: (parentKey: String) -> Int,
    onFolderClick: (Folder) -> Unit,
    addFolder: AddFolder,
    editFolder: EditFolder
) {
    Column(modifier = Modifier.background(backgroundColor)) {
        folders(parentKey).forEach { folder ->
            DrawerFolder(
                parentKey = parentKey,
                folder = folder,
                onFolderClick = onFolderClick,
                startPadding = startPadding,
                backgroundColor = backgroundColor,
                folders = folders,
                folderNames = folderNames,
                foldersSize = foldersSize,
                itemSize = itemSize,
                addFolder = addFolder,
                editFolder = editFolder
            )
        }
    }
}