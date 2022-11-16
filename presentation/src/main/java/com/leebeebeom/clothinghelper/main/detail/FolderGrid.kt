package com.leebeebeom.clothinghelper.main.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.base.SingleLineText
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.toStable
import com.leebeebeom.clothinghelperdomain.model.getDummyFolders

//TODO 탑 팹

@Composable
fun FolderGrid(folders: List<StableFolder> = getDummyFolders().map { it.toStable() }) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = folders) { // TODO 키
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CardWithFolderShape(size = 72.dp, elevation = 2.dp, onClick = { /*TODO*/ }) {
                    TotalCount()
                }
                Name { it.name }
            }
        }
    }
}

@Composable
private fun Name(name: () -> String) {
    SingleLineText(
        text = name(),
        style = MaterialTheme.typography.body2.copy(fontSize = 12.sp)
    )
}

@Composable
private fun TotalCount() {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.padding(end = 4.dp, bottom = 4.dp)
    ) {
        SingleLineText(
            text = "10", // TODO
            style = MaterialTheme.typography.caption.copy(
                color = LocalContentColor.current.copy(ContentAlpha.medium),
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )
        )
    }
}