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
    // TODO 커서 포지션(텍스트 셀렉트),  처음 다이얼로그 보일 시 확인 버튼 disable(변경되어야 enable)
    DialogRoot(onDismissDialog = onDismissDialog) {
        DialogTitle(text = R.string.edit_category_name)
        DialogTextField(
            text = categoryName,
            error = error,
            onTextChange = onCategoryNameChange,
            label = R.string.category,
            placeHolder = R.string.category_place_holder
        )
        DialogTextButtons(
            positiveButtonEnabled = positiveButtonEnabled,
            onPositiveButtonClick = onPositiveButtonClick,
            onDismissDialog = onDismissDialog
        )
    }
}