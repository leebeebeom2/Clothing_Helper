package com.leebeebeom.clothinghelper.main.root.contents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.composables.SimpleIcon
import com.leebeebeom.clothinghelper.base.composables.SingleLineText
import com.leebeebeom.clothinghelper.main.root.components.DrawerContentRow
import com.leebeebeom.clothinghelper.main.root.components.DrawerCount
import com.leebeebeom.clothinghelper.main.root.components.DrawerExpandIcon
import com.leebeebeom.clothinghelper.main.root.components.DrawerItems
import com.leebeebeom.clothinghelper.main.root.contents.dropdownmenus.DrawerFolderDropDownMenu
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.theme.DarkGray
import com.leebeebeom.clothinghelper.util.AddFolder
import com.leebeebeom.clothinghelper.util.EditFolder
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerFolder(
    folder: () -> StableFolder,
    onClick: (StableFolder) -> Unit,
    startPadding: Dp,
    folders: (key: String) -> ImmutableList<StableFolder>,
    onAddFolderPositiveClick: AddFolder,
    onEditFolderPositiveClick: EditFolder,
) {

    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var showDropDownMenu by rememberSaveable { mutableStateOf(false) }

    DrawerContentRow(modifier = Modifier
        .heightIn(40.dp)
        .padding(start = startPadding),
        onClick = { onClick(folder()) },
        onLongClick = { showDropDownMenu = true }) {

        SimpleIcon(drawable = R.drawable.ic_folder, modifier = Modifier.size(25.dp))

        Column(
            Modifier
                .padding(start = 8.dp)
                .padding(vertical = 8.dp)
                .weight(1f)
        ) {
            SingleLineText(
                text = folder().name, style = MaterialTheme.typography.subtitle2
            )
            DrawerFolderDropDownMenu(
                show = { showDropDownMenu },
                onDismiss = { showDropDownMenu = false },
                folders = { folders(folder().key) },
                onAddFolderPositiveClick = { parentKey, subCategoryKey, name, parent ->
                    onAddFolderPositiveClick(parentKey, subCategoryKey, name, parent)
                    isExpanded = true
                },
                onEditFolderPositiveClick = onEditFolderPositiveClick,
                folder = folder
            )
            DrawerCount { folders(folder().key) }
        }
        DrawerExpandIcon(isLoading = { false },
            isExpanded = { isExpanded },
            onClick = { isExpanded = !isExpanded },
            items = { folders(folder().key) })
    }

    DrawerItems(show = { isExpanded }, items = { folders(folder().key) }, backGround = DarkGray
    ) {
        DrawerFolder(
            folder = { it },
            onClick = onClick,
            startPadding = startPadding.plus(8.dp),
            folders = folders,
            onAddFolderPositiveClick = onAddFolderPositiveClick,
            onEditFolderPositiveClick = onEditFolderPositiveClick
        )
    }
}