package com.leebeebeom.clothinghelper.ui.component.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.ui.component.dialog.component.CustomDialog

@Composable
fun DeleteFolderDialog(
    onDismiss: () -> Unit,
    toBeDeletedFolders: List<Folder>,
    show: () -> Boolean,
    onPositiveButtonClick: () -> Unit
) {
    if (show())
        CustomDialog(
            onDismiss = onDismiss,
            title = R.string.delete_folder,
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.delete_folder_dialog_text,
                            toBeDeletedFolders.size
                        )
                    )
                    Text(
                        text = stringResource(id = R.string.delete_folder_dialog_text2),
                        style = MaterialTheme.typography.caption.copy(
                            LocalContentColor.current.copy(
                                ContentAlpha.disabled
                            )
                        )
                    )
                }
            },
            onPositiveButtonClick = onPositiveButtonClick,
            positiveButtonColor = MaterialTheme.colors.error,
            cancelButtonColor = Color.Unspecified
        )
}