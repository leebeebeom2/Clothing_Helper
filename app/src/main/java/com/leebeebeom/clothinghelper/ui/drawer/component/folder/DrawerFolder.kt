package com.leebeebeom.clothinghelper.ui.drawer.component.folder

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.ui.component.IconWrapper
import com.leebeebeom.clothinghelper.ui.drawer.DrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerExpandIcon
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerItemsWrapperWithExpandAnimation
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerRow
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerTextWithDoubleCount
import com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus.DrawerFolderDropDownMenu
import com.leebeebeom.clothinghelper.ui.drawer.rememberDrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import com.leebeebeom.clothinghelper.ui.util.EditFolder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun DrawerFolder(
    parentKey: String,
    folder: Folder,
    onFolderClick: (Folder) -> Unit,
    startPadding: Dp,
    backgroundColor: Color,
    folders: (parentKey: String) -> ImmutableList<Folder>,
    folderNames: (parentKey: String) -> ImmutableSet<String>,
    foldersSize: (parentKey: String) -> Int,
    itemSize: (parentKey: String) -> Int,
    addFolder: AddFolder,
    editFolder: EditFolder,
    state: DrawerItemDropdownMenuState = rememberDrawerItemDropdownMenuState(),
) {
    val childFolderSize by remember { derivedStateOf { foldersSize(folder.key) } }
    val childFolderNames by remember { derivedStateOf { folderNames(folder.key) } }
    val childItemSizeMap by remember { derivedStateOf { itemSize(folder.key) } }

    DrawerRow(
        modifier = Modifier.padding(start = startPadding),
        onClick = { onFolderClick(folder) },
        onLongClick = state::onLongClick,
        onSizeChange = state::onSizeChanged,
    ) {
        IconWrapper(drawable = R.drawable.ic_folder, modifier = Modifier.size(32.dp))

        DrawerTextWithDoubleCount(
            modifier = Modifier.padding(start = 8.dp),
            text = { folder.name },
            style = MaterialTheme.typography.body1,
            folderSize = { childFolderSize },
            itemSize = { childItemSizeMap }
        )

        DrawerExpandIcon(expanded = { state.expanded },
            toggleExpand = state::toggleExpand,
            dataSize = { childFolderSize })

        DrawerFolderDropDownMenu(
            state = state,
            selectedFolder = { folder },
            onDismiss = state::onDismissDropDownMenu,
            folderNames = { folderNames(parentKey) },
            childFolderNames = { childFolderNames },
            addFolder = addFolder,
            editFolder = editFolder,
            expand = state::expand
        )
    }

    SubFoldersWrapper(
        childFoldersSize = { childFolderSize },
        state = state,
        folder = folder,
        folders = folders,
        childFolderNames = childFolderNames,
        startPadding = startPadding,
        backgroundColor = backgroundColor,
        foldersSize = foldersSize,
        itemSize = itemSize,
        onFolderClick = onFolderClick,
        addFolder = addFolder,
        editFolder = editFolder
    )
}

@Composable
private fun SubFoldersWrapper(
    childFoldersSize: () -> Int,
    state: DrawerItemDropdownMenuState,
    folder: Folder,
    folders: (parentKey: String) -> ImmutableList<Folder>,
    childFolderNames: ImmutableSet<String>,
    startPadding: Dp,
    backgroundColor: Color,
    foldersSize: (parentKey: String) -> Int,
    itemSize: (parentKey: String) -> Int,
    onFolderClick: (Folder) -> Unit,
    addFolder: AddFolder,
    editFolder: EditFolder
) {
    if (childFoldersSize() != 0)
        DrawerItemsWrapperWithExpandAnimation(expand = { state.expanded }) {
            DrawerFolders(
                parentKey = folder.key,
                folders = folders,
                folderNames = { childFolderNames },
                startPadding = startPadding.plus(12.dp),
                backgroundColor = backgroundColor.copy(
                    red = backgroundColor.red + 0.02f,
                    green = backgroundColor.green + 0.02f,
                    blue = backgroundColor.blue + 0.02f
                ),
                foldersSize = foldersSize,
                itemSize = itemSize,
                onFolderClick = onFolderClick,
                addFolder = addFolder,
                editFolder = editFolder,
            )
        }
}