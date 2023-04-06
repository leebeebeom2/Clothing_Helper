package com.leebeebeom.clothinghelper.ui.drawer.component.mainmenu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.ui.component.SingleLineText
import com.leebeebeom.clothinghelper.ui.drawer.DrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.drawer.component.*
import com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus.DrawerDropdownMenu
import com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus.DrawerDropdownMenuAddFolder
import com.leebeebeom.clothinghelper.ui.drawer.rememberDrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun DrawerArchive(
    archive: MainMenu,
    onArchiveClick: (MainMenuType) -> Unit,
    archiveFoldersSize: () -> Int,
    archiveFolderNames: () -> ImmutableSet<String>,
    archiveItemsSize: () -> Int,
    addFolder: AddFolder,
    state: DrawerItemDropdownMenuState = rememberDrawerItemDropdownMenuState(),
    archiveFolders: @Composable () -> Unit,
) {
    val localFoldersSize by remember { derivedStateOf(archiveFoldersSize) }

    DrawerRow(
        onClick = { onArchiveClick(archive.type) },
        onLongClick = state::onLongClick,
        onSizeChange = state::onSizeChanged
    ) {
        DrawerInsideRow {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp)
            ) {
                SingleLineText(text = archive.name, style = drawerMainMenuTextStyle())
                DrawerCount(folderSize = { localFoldersSize }, itemSize = { archiveItemsSize() })
            }
        }
        DrawerExpandIcon(expanded = { state.expanded },
            toggleExpand = state::toggleExpand,
            dataSize = { localFoldersSize })
    }

    DrawerDropdownMenu(state = state, onDismiss = state::onDismiss) {
        DrawerDropdownMenuAddFolder(
            onDismiss = state::onDismiss,
            folderNames = archiveFolderNames,
            onPositiveButtonClick = { name ->
                addFolder(archive.type.name, name)
            }
        )
    }

    DrawerItemsWrapperWithExpandAnimation(expand = { state.expanded }) { archiveFolders() }
}