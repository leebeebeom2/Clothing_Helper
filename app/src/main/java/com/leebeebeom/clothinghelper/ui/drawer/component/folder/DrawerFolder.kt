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
import com.leebeebeom.clothinghelper.ui.util.FoldersMap
import com.leebeebeom.clothinghelper.ui.util.FoldersSizeMap
import kotlinx.collections.immutable.*

@Composable
fun DrawerFolder(
    folder: Folder,
    onFolderClick: (Folder) -> Unit,
    startPadding: Dp,
    backgroundColor: Color,
    foldersMap: () -> FoldersMap,
    folderNames: () -> ImmutableSet<String>,
    folderSizeMap: () -> FoldersSizeMap,
    folderNamesMap: () -> ImmutableMap<String, ImmutableSet<String>>,
    itemSize: () -> ImmutableMap<String, Int>,
    addFolder: AddFolder,
    editFolder: EditFolder,
    state: DrawerItemDropdownMenuState = rememberDrawerItemDropdownMenuState(),
) {
    val childFolderSize by remember {
        derivedStateOf {
            folderSizeMap().getOrDefault(key = folder.key, defaultValue = 0)
        }
    }
    val childFolderNames by remember {
        derivedStateOf {
            folderNamesMap().getOrDefault(key = folder.key, defaultValue = persistentSetOf())
        }
    }
    val childItemSizeMap by remember {
        derivedStateOf { itemSize().getOrDefault(key = folder.key, defaultValue = 0) }
    }

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
            onDismiss = state::onDismiss,
            folderNames = folderNames,
            childFolderNames = { childFolderNames },
            addFolder = { parentKey, name ->
                addFolder(parentKey, name)
                state.expand()
            },
            editFolder = editFolder
        )
    }

    DrawerItemsWrapperWithExpandAnimation(expand = { state.expanded }) {
        DrawerFolders(
            folders = { foldersMap().getOrDefault(folder.key, persistentListOf()) },
            folderNames = { childFolderNames },
            startPadding = startPadding.plus(12.dp),
            backgroundColor = backgroundColor.copy(
                red = backgroundColor.red + 0.02f,
                green = backgroundColor.green + 0.02f,
                blue = backgroundColor.blue + 0.02f
            ),
            foldersMap = foldersMap,
            foldersSizeMap = folderSizeMap,
            folderNamesMap = folderNamesMap,
            itemSizeMap = { persistentMapOf() },
            onFolderClick = onFolderClick,
            addFolder = addFolder,
            editFolder = editFolder,
        )
    }
}