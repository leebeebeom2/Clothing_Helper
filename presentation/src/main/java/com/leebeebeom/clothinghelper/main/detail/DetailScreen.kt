package com.leebeebeom.clothinghelper.main.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.theme.ClothingHelperTheme
import com.leebeebeom.clothinghelperdomain.model.getDummyFolders

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailScreen(
    subCategoryName: String,
    subCategoryKey: String
) {
    val backdropScaffoldState =
        rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)
    BackdropScaffold(
        scaffoldState = backdropScaffoldState,
        appBar = { Header { subCategoryName } },
        backLayerBackgroundColor = MaterialTheme.colors.background,
        backLayerContent = { FolderGrid() },
        frontLayerContent = { Text(text = "front layer contents") },
        frontLayerElevation = 4.dp,
        frontLayerScrimColor = Color.Unspecified
    )
}

@Composable
fun FrontLayerContent() {
    Column {

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FolderGrid() {
    LazyVerticalGrid(columns = GridCells.Fixed(4), contentPadding = PaddingValues(bottom = 20.dp)) {
        items(getDummyFolders()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Card(
                    shape = FolderShape(),
                    modifier = Modifier.size(80.dp),
                    elevation = 4.dp,
                    onClick = {}) {}
                Text(text = "이름")
            }
        }
    }
}

@Composable
private fun Header(title: () -> String) {
    Text(
        text = title(),
        style = MaterialTheme.typography.h5,
        fontSize = 26.sp,
        letterSpacing = 1.5.sp,
        fontWeight = FontWeight.Normal,
        modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FolderPreview() {
    ClothingHelperTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(12.dp)
        ) {
            Card(shape = FolderShape(), modifier = Modifier.size(100.dp), elevation = 2.dp) {
                Text(text = "ㅎㅇ")
                Text(text = "ㅎㅇ")
                Text(text = "ㅎㅇ")
            }
        }
    }
}