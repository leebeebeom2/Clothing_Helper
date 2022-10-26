package com.leebeebeom.clothinghelper.main.subcategory

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.DialogRoot
import com.leebeebeom.clothinghelper.base.DialogTextButtons
import com.leebeebeom.clothinghelper.base.DialogTextField
import com.leebeebeom.clothinghelper.base.DialogTitle

@Composable
fun EditSubCategoryNameDialog(
    categoryName: String,
    @StringRes error: Int?,
    onCategoryNameChange: (String) -> Unit,
    onPositiveButtonClick: () -> Unit,
    onDismissDialog: () -> Unit,
    positiveButtonEnabled: Boolean
) {
    // TODO 커서 포지션, 다이얼로그 높이, 처음 다이얼로그 보일 시 확인 버튼 disable(변경되어야 enable)
    DialogRoot(onDismissDialog = onDismissDialog) {
        DialogTitle(title = R.string.edit_category_name)
        DialogTextField(
            categoryName = categoryName,
            error = error,
            onCategoryNameChange = onCategoryNameChange
        )
        DialogTextButtons(
            positiveButtonEnabled = positiveButtonEnabled,
            onPositiveButtonClick = onPositiveButtonClick,
            onDismissDialog = onDismissDialog
        )
    }
}