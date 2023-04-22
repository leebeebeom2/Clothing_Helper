package com.leebeebeom.clothinghelper.ui.drawer.content.folder

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
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
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import com.leebeebeom.clothinghelper.ui.util.EditFolder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet

const val DrawerFolderTag = "drawer folder"

@Composable
fun DrawerFolder( // skippable
    parentKey: String,
    folder: Folder,
    basePadding: Dp,
    backgroundColor: Color,
    onFolderClick: (Folder) -> Unit,
    folders: (parentKey: String) -> ImmutableList<Folder>,
    folderNames: (parentKey: String) -> ImmutableSet<String>,
    foldersSize: (parentKey: String) -> Int,
    itemsSize: (parentKey: String) -> Int,
    addFolder: AddFolder,
    editFolder: EditFolder,
    deleteFolder: (Folder) -> Unit,
    height: () -> Dp,
    state: DrawerItemDropdownMenuState
) {
    val localFolderNames by remember(folderNames) { derivedStateOf { folderNames(parentKey) } }

    val childFolderNames by remember(folderNames) { derivedStateOf { folderNames(folder.key) } }
    val childFoldersSize by remember(foldersSize) { derivedStateOf { foldersSize(folder.key) } }
    val childItemsSize by remember(itemsSize) { derivedStateOf { itemsSize(folder.key) } }
    val startPadding = remember { basePadding.plus(12.dp) }

    DrawerRow(
        modifier = Modifier
            .padding(start = startPadding)
            .testTag(DrawerFolderTag),
        onClick = { onFolderClick(folder) },
        onLongClick = state::onLongClick,
        onSizeChange = state::onSizeChanged,
        height = height
    ) {
        IconWrapper(
            drawable = R.drawable.ic_folder,
            modifier = Modifier
                .size(36.dp)
                .padding(end = 6.dp)
        )

        DrawerTextWithDoubleCount(
            text = { folder.name },
            style = MaterialTheme.typography.body1,
            foldersSize = { childFoldersSize },
            itemsSize = { childItemsSize })

        DrawerExpandIcon(expanded = { state.expanded },
            toggleExpand = state::toggleExpand,
            dataSize = { childFoldersSize })

        DrawerDownMenuEditAndAddFolder(
            state = state,
            selectedFolder = { folder },
            onDismiss = state::onDismissDropDownMenu,
            folderNames = { localFolderNames },
            childFolderNames = { childFolderNames },
            addFolder = addFolder,
            editFolder = editFolder,
            deleteFolder = deleteFolder,
            expand = state::expand
        )
    }

    val draw by remember(foldersSize) { derivedStateOf { foldersSize(folder.key) > 0 } }

    DrawerItemsWrapperWithExpandAnimation(expand = { state.expanded }, draw = { draw }, item = {
        DrawerFolders(
            parentKey = folder.key,
            folders = folders,
            folderNames = folderNames,
            basePadding = startPadding,
            backgroundColor = getSubBackgroundColor(backgroundColor),
            foldersSize = foldersSize,
            itemsSize = itemsSize,
            onFolderClick = onFolderClick,
            addFolder = addFolder,
            editFolder = editFolder,
            deleteFolder = deleteFolder,
            height = height
        )
    })
}

fun getSubBackgroundColor(superColor: Color) = superColor.copy(
    red = superColor.red + 0.02f,
    green = superColor.green + 0.02f,
    blue = superColor.blue + 0.02f
)