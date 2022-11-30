package com.leebeebeom.clothinghelper.main.sizeChart

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.composables.SingleLineText
import com.leebeebeom.clothinghelper.main.base.dialogs.composables.DialogRoot
import com.leebeebeom.clothinghelper.main.base.dialogs.composables.DialogTextButton
import com.leebeebeom.clothinghelper.main.base.dialogs.composables.DialogTitle
import java.io.File

@Composable
fun AddPhoto(show: () -> Boolean, onDismiss: () -> Unit, onResult: (ActivityResult) -> Unit) {
    if (show()) {
        val photoLauncher = photoLauncher(onResult = {
            onResult(it)
            onDismiss()
        })
        val context = LocalContext.current

        DialogRoot(onDismiss = onDismiss) {
            DialogTitle(text = R.string.add_photo)
            SingleLineText(
                text = R.string.choose_ratio,
                style = MaterialTheme.typography.body2.copy(
                    fontSize = 16.sp,
                    color = LocalContentColor.current.copy(0.8f)
                ),
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .padding(start = 8.dp),
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                DialogTextButton(
                    text = R.string.square_ratio,
                    onClick = {
                        photoLauncher.launch(getPhotoIntent(context, 1, 1))
                    }
                )
                DialogTextButton(
                    text = R.string.rectangle_ratio,
                    onClick = {
                        photoLauncher.launch(getPhotoIntent(context, 3, 4))
                    }
                )
            }
        }
    }
}

@Composable
private fun photoLauncher(onResult: (ActivityResult) -> Unit) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult(), onResult = onResult
)

private fun getPhotoIntent(context: Context, x: Int, y: Int): Intent {
    val path = context.cacheDir
    val fileName = File.createTempFile("${System.currentTimeMillis()}", ".jpg", path)
    val contentUri =
        FileProvider.getUriForFile(context, "com.leebeebeom.clothinghelper.fileprovider", fileName)

    val intent = Intent(Intent.ACTION_PICK)
        .setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        .putExtra("crop", true)
        .putExtra("aspectX", x)
        .putExtra("aspectY", y)
        .putExtra(MediaStore.EXTRA_OUTPUT, contentUri)

    val activity = intent.resolveActivity(context.packageManager)
    context.grantUriPermission(
        activity.packageName,
        contentUri,
        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
    )

    return intent
}