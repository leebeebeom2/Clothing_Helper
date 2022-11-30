package com.leebeebeom.clothinghelper.main.sizeChart

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.composables.SimpleIcon
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.rememberDrawablePainter
import java.io.File

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SizeChartPicture(
    viewModel: SizeChartViewModel = hiltViewModel(), uiState: SizeChartUIState = viewModel.uiState
) {
    val photoLauncher = photoLauncher(onResult = viewModel::getPicture)
    val context = LocalContext.current

    Box(modifier = Modifier.padding(horizontal = 40.dp, vertical = 20.dp)) {
        Card(modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = 2.dp,
            onClick = {
                photoLauncher.launch(getPhotoIntent(context))
            }) {
            Picture { uiState.imageUri }
        }
    }
}

@Composable
private fun photoLauncher(onResult: (ActivityResult) -> Unit) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult(), onResult = onResult
)

private fun getPhotoIntent(context: Context): Intent {
    val path = context.cacheDir
    val fileName = File.createTempFile("${System.currentTimeMillis()}", ".jpg", path)
    val contentUri =
        FileProvider.getUriForFile(context, "com.leebeebeom.clothinghelper.fileprovider", fileName)

    val intent =
        Intent(ACTION_PICK).setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            .putExtra("crop", true).putExtra("aspectX", 3).putExtra("aspectY", 4)
            .putExtra(MediaStore.EXTRA_OUTPUT, contentUri)

    val activity = intent.resolveActivity(context.packageManager)
    context.grantUriPermission(
        activity.packageName,
        contentUri,
        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
    )

    return intent
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