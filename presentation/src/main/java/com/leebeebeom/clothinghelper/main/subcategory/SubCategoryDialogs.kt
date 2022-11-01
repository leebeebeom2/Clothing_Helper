package com.leebeebeom.clothinghelper.main.subcategory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.DialogRoot
import com.leebeebeom.clothinghelper.base.DialogTextButtons
import com.leebeebeom.clothinghelper.base.DialogTextField
import com.leebeebeom.clothinghelper.base.DialogTitle
import com.leebeebeom.clothinghelperdomain.model.SubCategory

@Composable
fun SubCategoryTextFieldDialog(
    state: BaseSubCategoryTextFieldDialogState,
    subCategories: List<SubCategory>,
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

    LaunchedEffect(key1 = state.textFiled, key2 = subCategories) {
        if (subCategories.map { it.name }
                .contains(state.textFiled.text.trim())) state.updateError(R.string.error_same_category_name)
    }
}