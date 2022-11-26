package com.leebeebeom.clothinghelper.main.detail.contents.folder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.base.Anime
import com.leebeebeom.clothinghelper.base.composables.SingleLineText
import com.leebeebeom.clothinghelper.main.base.composables.CircleCheckBox
import com.leebeebeom.clothinghelper.main.detail.CardWithFolderShape
import com.leebeebeom.clothinghelper.map.StableFolder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet

@Composable
fun Folder(
    selectedFolderKeys: () -> ImmutableSet<String>,
    folder: StableFolder,
    isSelectMode: () -> Boolean,
    onSelect: (String) -> Unit,
    onClick: (StableFolder) -> Unit,
    folders: (parentKey: String) -> ImmutableList<StableFolder>
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val isChecked by remember {
            derivedStateOf { selectedFolderKeys().contains(folder.key) }
        }
        CardWithFolderShape(
            size = 60.dp,
            elevation = 2.dp,
            onClick = { if (isSelectMode()) onSelect(folder.key) else onClick(folder) }) {
            Box(modifier = Modifier.padding(end = 4.dp, bottom = 4.dp)) {
                FolderCheckBox(
                    isChecked = isChecked,
                    show = isSelectMode,
                    onClick = { onSelect(folder.key) })
                FolderCount { folders(folder.key) }
            }
        }
        FolderName { folder.name }
    }
}

@Composable
private fun BoxScope.FolderCheckBox(
    isChecked: Boolean,
    show: () -> Boolean,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = show(),
        enter = Anime.CircleCheckBox.scaleIn,
        exit = Anime.CircleCheckBox.scaleOut
    ) {
        key("folderCheckBox") {
            CircleCheckBox(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 1.dp),
                isChecked = { isChecked },
                onClick = onClick,
                size = 16.dp
            )
        }
    }
}

@Composable
private fun BoxScope.FolderCount(folders: () -> ImmutableList<StableFolder>) {
    Column(modifier = Modifier
        .align(Alignment.BottomEnd)) {
        ProvideTextStyle(
            value = MaterialTheme.typography.caption.copy(
                color = LocalContentColor.current.copy(ContentAlpha.medium),
                fontSize = 10.sp
            )
        ) {
            SingleLineText(text = "${folders().size}")
            SingleLineText(text = "0")
        }
    }
}

@Composable
private fun FolderName(name: () -> String) {
    Text(
        modifier = Modifier.widthIn(0.dp, 68.dp),
        text = name(),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.body2.copy(fontSize = 12.sp)
    )
}