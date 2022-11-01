package com.leebeebeom.clothinghelper.main.subcategory

import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.base.DialogRoot
import com.leebeebeom.clothinghelper.base.DialogTextButtons
import com.leebeebeom.clothinghelper.base.DialogTextField
import com.leebeebeom.clothinghelper.base.DialogTitle

@Composable
fun SubCategoryTextFieldDialog(
    state: BaseSubCategoryTextFieldDialogState,
    onPositiveButtonClick: () -> Unit,
) {
    DialogRoot(onDismissDialog = state::onDismissDialog) {
        DialogTitle(text = state.title)
        DialogTextField(state)
        DialogTextButtons(
            positiveButtonEnabled = state.positiveButtonEnabled,
            onPositiveButtonClick = onPositiveButtonClick,
            onDismissDialog = state::onDismissDialog
        )
    }
}