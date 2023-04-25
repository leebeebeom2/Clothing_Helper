package com.leebeebeom.clothinghelper.ui.drawer.component

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.model.MenuType
import com.leebeebeom.clothinghelper.ui.component.dialog.AddFolderDialog
import com.leebeebeom.clothinghelper.ui.drawer.DrawerItemDropdownMenuState
import com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus.DrawerDropdownMenu
import com.leebeebeom.clothinghelper.ui.drawer.component.dropdownmenus.DrawerDropdownMenuItem
import com.leebeebeom.clothinghelper.ui.util.AddFolder
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun DrawerContentWithDoubleCount(
    modifier: Modifier = Modifier,
    backgroundColor: () -> Color = { Color.Transparent },
    key: String,
    menuType: MenuType,
    @StringRes text: Int,
    textStyle: TextStyle,
    onClick: () -> Unit,
    foldersSize: (parentKey: String) -> Int,
    folderNames: (parentKey: String) -> ImmutableSet<String>,
    itemsSize: (parentKey: String) -> Int,
    addDotIcon: Boolean = false,
    addFolder: AddFolder,
    state: DrawerItemDropdownMenuState,
    height: () -> Dp,
    folders: @Composable () -> Unit
) {
    val localFolderNames by remember(folderNames) { derivedStateOf { folderNames(key) } }
    val localFoldersSize by remember(foldersSize) { derivedStateOf { foldersSize(key) } }
    val localItemsSize by remember(itemsSize) { derivedStateOf { itemsSize(key) } }

    DrawerRow(
        modifier = modifier,
        backgroundColor = backgroundColor,
        onClick = onClick,
        onLongClick = state::onLongClick,
        onSizeChange = state::onSizeChanged,
        height = height
    ) {
        if (addDotIcon) DrawerDotIcon()

        DrawerTextWithDoubleCount(text = text,
            style = textStyle,
            foldersSize = { localFoldersSize },
            itemsSize = { localItemsSize })

        DrawerExpandIcon(expanded = { state.expanded },
            toggleExpand = state::toggleExpand,
            dataSize = { localFoldersSize })

        var showAddDialog by rememberSaveable { mutableStateOf(false) }

        DrawerDropdownMenu(state = state, onDismiss = state::onDismissDropDownMenu) {
            DrawerDropdownMenuItem(text = R.string.dropdown_menu_add_folder) {
                showAddDialog = true
                state.onDismissDropDownMenu()
            }
        }

        AddFolderDialog(
            folderNames = { localFolderNames },
            onPositiveButtonClick = { name ->
                addFolder(key, name, menuType)
                state.expand()
            }, show = { showAddDialog }, onDismiss = { showAddDialog = false })
    }

    val draw by remember(foldersSize) { derivedStateOf { foldersSize(key) > 0 } }

    DrawerItemsWrapperWithExpandAnimation(
        expand = { state.expanded },
        item = { folders() },
        draw = { draw })
}
