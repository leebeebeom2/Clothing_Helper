package com.leebeebeom.clothinghelper.main.detail

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.TAG
import com.leebeebeom.clothinghelperdomain.model.getDummyFolders

// TODO 앱바 엘리베이션 구현

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
        frontLayerScrimColor = Color.Unspecified,
        headerHeight = 80.dp
    )
}

@Composable
fun FrontLayerContent() {
    Column {

    }
}

@Composable
fun FolderGrid() {
    val state = rememberLazyGridState()
    val isScrollingUp = state.isScrollingUp()
    if (isScrollingUp)
        Log.d(TAG, "FolderGrid: 업")
    else Log.d(TAG, "FolderGrid: 다운")

    LazyVerticalGrid(
        state = state,
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(getDummyFolders()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CardWithFolderShape(size = 68.dp, elevation = 2.dp, onClick = {}) {
                    // TODO 컨텐츠 갯수
                }
                Text(text = it.name)
            }//TODO 탑 팹
        }
    }
}

@Composable
private fun Header(title: () -> String) {
    TopAppBar(backgroundColor = MaterialTheme.colors.background, elevation = 0.dp) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp), contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "TOPs - 반팔",
                style = MaterialTheme.typography.h2,
                fontSize = 30.sp,
                color = Color.Black
            )
//            Text(
//                text = title(),
//                style = MaterialTheme.typography.h5,
//                fontSize = 24.sp,
//                letterSpacing = 1.5.sp,
//                fontWeight = FontWeight.Normal,
//                color = Color.Black
//            )
        }
    }
}

@Composable
private fun LazyGridState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}
