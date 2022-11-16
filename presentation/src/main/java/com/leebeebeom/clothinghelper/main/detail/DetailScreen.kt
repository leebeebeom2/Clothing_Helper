package com.leebeebeom.clothinghelper.main.detail

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.base.SingleLineText
import com.leebeebeom.clothinghelper.util.getHeaderStringRes
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent

// TODO 앱바 엘리베이션 구현

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailScreen(
    parent: SubCategoryParent, title: String, parentKey: String
) {
    Box {
        val backdropScaffoldState =
            rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)
        BackdropScaffold(
            scaffoldState = backdropScaffoldState,
            appBar = { Header(parent, title) },
            backLayerBackgroundColor = MaterialTheme.colors.background,
            backLayerContent = { FolderGrid() },
            frontLayerContent = { FrontLayerContent() },
            frontLayerElevation = 4.dp,
            frontLayerScrimColor = Color.Unspecified,
            headerHeight = 52.dp
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 24.dp, bottom = 32.dp)
        ) {
            AddFab()
        }
    }
}

@Composable
private fun Header(parent: SubCategoryParent, title: String) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
        contentPadding = PaddingValues(horizontal = 12.dp),
        modifier = Modifier.horizontalScroll(rememberScrollState()),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val parentText = stringResource(id = getHeaderStringRes(parent))
            SingleLineText(
                text = "$parentText$title",
                style = MaterialTheme.typography.h2.copy(color = Color.Black, fontSize = 24.sp)
            )
        }
    }
}