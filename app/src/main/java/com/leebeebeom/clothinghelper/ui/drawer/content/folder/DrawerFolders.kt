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
import com.leebeebeom.clothinghelper.ui.drawer.rememberDrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import com.leebeebeom.clothinghelper.ui.util.CurrentBackStack
import com.leebeebeom.clothinghelper.ui.util.DeleteFolder
import com.leebeebeom.clothinghelper.ui.util.EditFolder
import com.leebeebeom.clothinghelper.ui.util.FolderNames
import com.leebeebeom.clothinghelper.ui.util.Folders
import com.leebeebeom.clothinghelper.ui.util.FoldersSize
import com.leebeebeom.clothinghelper.ui.util.ItemsSize
import com.leebeebeom.clothinghelper.ui.util.OnFolderClick

@Composable
fun DrawerFolders( // skippable
    parentKey: String,
    basePadding: Dp,
    backgroundColor: Color,
    onFolderClick: OnFolderClick,
    folders: Folders,
    folderNames: FolderNames,
    foldersSize: FoldersSize,
    itemsSize: ItemsSize,
    addFolder: AddFolder,
    editFolder: EditFolder,
    deleteFolder: DeleteFolder,
    height: () -> Dp,
    currentBackStack: CurrentBackStack
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
                    deleteFolder = deleteFolder,
                    state = state,
                    height = height,
                    onLongClick = state::onLongClick,
                    onSizeChanged = state::onSizeChanged,
                    toggleExpand = state::toggleExpand,
                    onDismissDropdownMenu = state::onDismissDropdownMenu,
                    expand = state::expand,
                    currentBackStack = currentBackStack
                )
            }
        }
    }
}