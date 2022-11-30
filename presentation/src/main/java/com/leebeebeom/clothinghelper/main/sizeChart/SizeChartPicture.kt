package com.leebeebeom.clothinghelper.main.sizeChart

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.composables.SimpleIcon
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.rememberDrawablePainter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SizeChartPicture(
    viewModel: SizeChartViewModel = hiltViewModel(), uiState: SizeChartUIState = viewModel.uiState
) {
    var showRatioDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = 2.dp,
            onClick = {
                showRatioDialog = true
            }) {
            Picture { uiState.imageUri }
        }
    }

    AddPhotoDialog(show = { showRatioDialog }, onDismiss = { showRatioDialog = false }, onResult = viewModel::getPicture)
}

@Composable
private fun Picture(uri: () -> Uri?) {
    CoilImage(imageModel = uri(), modifier = Modifier.fillMaxWidth(), failure = {
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(1f), contentAlignment = Alignment.Center
        ) {
            SimpleIcon(
                drawable = R.drawable.ic_add_circle,
                modifier = Modifier.size(100.dp),
                tint = LocalContentColor.current.copy(alpha = 0.1f)
            )
        }
    }, success = {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = rememberDrawablePainter(drawable = it.drawable),
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
    })
}