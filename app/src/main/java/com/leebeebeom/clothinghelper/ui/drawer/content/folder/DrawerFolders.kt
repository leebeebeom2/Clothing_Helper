package com.leebeebeom.clothinghelper.ui.drawer.content.folder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.ui.drawer.rememberDrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import com.leebeebeom.clothinghelper.ui.util.EditFolder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun DrawerFolders( // skippable
    parentKey: String,
    basePadding: Dp,
    backgroundColor: Color,
    onFolderClick: (Folder) -> Unit,
    folders: (parentKey: String) -> ImmutableList<Folder>,
    folderNames: (parentKey: String) -> ImmutableSet<String>,
    foldersSize: (parentKey: String) -> Int,
    itemsSize: (parentKey: String) -> Int,
    addFolder: AddFolder,
    editFolder: EditFolder
) {
    val localFolders by remember(folders) { derivedStateOf { folders(parentKey) } }

    Column(modifier = Modifier.background(backgroundColor)) {
        localFolders.forEach { folder ->
            key(folder.key) {
                val state = rememberDrawerItemDropdownMenuState()

                DrawerFolder(
                    parentKey = parentKey,
                    folder = folder,
                    basePadding = basePadding,
                    backgroundColor = backgroundColor,
                    onFolderClick = onFolderClick,
                    folders = folders,
                    folderNames = folderNames,
                    foldersSize = foldersSize,
                    itemsSize = itemsSize,
                    addFolder = addFolder,
                    editFolder = editFolder,
                    state = state
                )
            }
        }
    }
}