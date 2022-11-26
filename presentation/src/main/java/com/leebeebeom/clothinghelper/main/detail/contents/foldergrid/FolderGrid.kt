package com.leebeebeom.clothinghelper.main.detail.contents.foldergrid

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.composables.SingleLineText
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelperdomain.model.Order
import com.leebeebeom.clothinghelperdomain.model.Sort
import com.leebeebeom.clothinghelperdomain.model.SortPreferences
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun FolderGrid(
    parentKey: String,
    folders: (parentKey: String) -> ImmutableList<StableFolder>,
    selectedFolderKeys: () -> ImmutableSet<String>,
    isSelectMode: () -> Boolean,
    sort: () -> SortPreferences,
    onSortClick: (Sort) -> Unit,
    onOrderClick: (Order) -> Unit,
    onSelect: (String) -> Unit,
    onLongClick: (String) -> Unit,
    onClick: (StableFolder) -> Unit
) {
    val isFolderEmpty by remember { derivedStateOf { folders(parentKey).isEmpty() } }

    Crossfade(targetState = isFolderEmpty) {
        if (!it)
            FolderGridContent(
                parentKey = parentKey,
                folders = folders,
                selectedFolderKeys = selectedFolderKeys,
                isSelectMode = isSelectMode,
                onSelect = onSelect,
                onLongClick = onLongClick,
                sort = sort,
                onSortClick = onSortClick,
                onOrderClick = onOrderClick,
                onClick = onClick
            )
        else FolderGridPlaceHolder()
    }
}

@Composable
private fun FolderGridPlaceHolder() {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 120.dp),
        contentAlignment = Alignment.Center
    ) {
        SingleLineText(
            text = R.string.folder_grid_place_holder,
            style = MaterialTheme.typography.body2.copy(
                color = LocalContentColor.current.copy(alpha = 0.6f)
            )
        )
    }
}