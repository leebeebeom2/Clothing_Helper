package com.leebeebeom.clothinghelper.main.subcategory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.TextFieldValue
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.*
import com.leebeebeom.clothinghelperdomain.model.SubCategory

@Composable
fun SubCategoryTextFieldDialog(
    state: BaseSubCategoryTextFieldDialogState,
    onPositiveButtonClick: () -> Unit,
) {
    DialogRoot(onDismissDialog = state::onDismissDialog) {
        DialogTitle(text = state.titleRes)
        DialogTextField(
            textFieldStateHolder = state.textFiledStateHolder,
            error = state.error,
            updateError = state::updateError
        )
        DialogTextButtons(
            positiveButtonEnabled = state.positiveButtonEnabled,
            onPositiveButtonClick = onPositiveButtonClick,
            onDismissDialog = state::onDismissDialog
        )
    }
}

abstract class BaseSubCategoryTextFieldDialogState {
    abstract val titleRes: Int
    abstract val textFiledStateHolder: MaxWidthTextFieldStateHolder
    protected abstract val errorState: MutableState<Int?>
    val error get() = errorState.value

    protected abstract var showDialogState: MutableState<Boolean>
    val showDialog get() = showDialogState.value

    protected abstract val subCategoriesState: List<SubCategory>

    open val positiveButtonEnabled get() = textFiledStateHolder.textState.isNotBlank() && error == null

    open fun showDialog() {
        showDialogState.value = true
    }

    fun onDismissDialog() {
        showDialogState.value = false
        textFiledStateHolder.onValueChange(TextFieldValue("")) { errorState.value = null }
    }

    open fun updateError(newName: String) {
        errorState.value = null
        if (subCategoriesState.map { it.name }.contains(newName.trim()))
            errorState.value = R.string.error_same_category_name
    }
}