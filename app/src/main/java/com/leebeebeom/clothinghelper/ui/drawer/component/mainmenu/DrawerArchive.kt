package com.leebeebeom.clothinghelper.ui.drawer.component.mainmenu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.leebeebeom.clothinghelper.ui.drawer.DrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerExpandIcon
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerItemsWrapperWithExpandAnimation
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerRow
import com.leebeebeom.clothinghelper.ui.drawer.component.DrawerTextWithDoubleCount
import com.leebeebeom.clothinghelper.ui.drawer.component.folder.DrawerArchiveDropdownMenu
import com.leebeebeom.clothinghelper.ui.drawer.rememberDrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun DrawerArchive(
    archive: MainMenu,
    onArchiveClick: (MainMenuType) -> Unit,
    folderNames: (parentKey: String) -> ImmutableSet<String>,
    foldersSize: (parentKey: String) -> Int,
    itemsSize: (parentKey: String) -> Int,
    addFolder: AddFolder,
    state: DrawerItemDropdownMenuState = rememberDrawerItemDropdownMenuState(),
    archiveFolders: @Composable () -> Unit,
) {
    val localFoldersSize by remember { derivedStateOf { foldersSize(archive.type.name) } }

    DrawerRow(
        onClick = { onArchiveClick(archive.type) },
        onLongClick = state::onLongClick,
        onSizeChange = state::onSizeChanged
    ) {
        DrawerTextWithDoubleCount(
            text = archive.name,
            style = drawerMainMenuTextStyle(),
            folderSize = { localFoldersSize },
            itemSize = { itemsSize(archive.type.name) }
        )

        DrawerExpandIcon(expanded = { state.expanded },
            toggleExpand = state::toggleExpand,
            dataSize = { localFoldersSize })

        DrawerArchiveDropdownMenu(
            state = state,
            folderNames = folderNames,
            archive = archive,
            addFolder = addFolder
        )
    }

    DrawerItemsWrapperWithExpandAnimation(expand = { state.expanded }) { archiveFolders() }
}