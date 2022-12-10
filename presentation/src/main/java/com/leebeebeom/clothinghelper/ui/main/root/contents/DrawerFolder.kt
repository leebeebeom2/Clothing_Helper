package com.leebeebeom.clothinghelper.ui.main.root.contents

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
import com.leebeebeom.clothinghelper.composable.SimpleIcon
import com.leebeebeom.clothinghelper.composable.SingleLineText
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.theme.DarkGray
import com.leebeebeom.clothinghelper.ui.main.root.components.DrawerCount
import com.leebeebeom.clothinghelper.ui.main.root.components.DrawerExpandIcon
import com.leebeebeom.clothinghelper.ui.main.root.components.DrawerItems
import com.leebeebeom.clothinghelper.ui.main.root.components.DrawerRow
import com.leebeebeom.clothinghelper.ui.main.root.contents.dropdownmenus.DrawerFolderDropDownMenu
import com.leebeebeom.clothinghelper.util.AddFolder
import com.leebeebeom.clothinghelper.util.EditFolder
import kotlinx.collections.immutable.ImmutableList

@Composable
fun DrawerFolder(
    folder: () -> StableFolder,
    onClick: (StableFolder) -> Unit,
    startPadding: Dp,
    folders: () -> ImmutableList<StableFolder>,
    allFolders: (key: String) -> ImmutableList<StableFolder>,
    onAddFolderPositiveClick: AddFolder,
    onEditFolderPositiveClick: EditFolder,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var showDropDownMenu by rememberSaveable { mutableStateOf(false) }

    DrawerRow(modifier = Modifier
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
                textString = { folder().name }, style = MaterialTheme.typography.subtitle2
            )
            DrawerFolderDropDownMenu(
                selectedFolder = folder,
                show = { showDropDownMenu },
                onDismiss = { showDropDownMenu = false },
                folders = folders,
                onAddFolderPositiveClick = { parentKey, subCategoryKey, name, parent ->
                    onAddFolderPositiveClick(parentKey, subCategoryKey, name, parent)
                    isExpanded = true
                },
                onEditFolderPositiveClick = onEditFolderPositiveClick
            )
            DrawerCount(folders = folders)
        }
        DrawerExpandIcon(
            isLoading = { false },
            isExpanded = { isExpanded },
            onClick = { isExpanded = !isExpanded },
            items = folders
        )
    }

    DrawerItems(
        show = { isExpanded }, items = folders, background = DarkGray
    ) {
        DrawerFolder(
            folder = { it },
            onClick = onClick,
            startPadding = startPadding.plus(8.dp),
            folders = folders,
            allFolders = allFolders,
            onAddFolderPositiveClick = onAddFolderPositiveClick,
            onEditFolderPositiveClick = onEditFolderPositiveClick
        )
    }
}