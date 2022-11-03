package com.leebeebeom.clothinghelper.main.subcategory

import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.TextFieldValue
import com.leebeebeom.clothinghelper.base.MaxWidthTextFieldStateHolder

abstract class BaseSubCategoryTextFieldDialogStateHolder {
    abstract val titleRes: Int
    abstract val textFiledStateHolder: MaxWidthTextFieldStateHolder
    protected abstract val errorState: MutableState<Int?>
    val error get() = errorState.value

    protected abstract var showDialogState: MutableState<Boolean>
    val showDialog get() = showDialogState.value

    open val positiveButtonEnabled get() = textFiledStateHolder.textState.isNotBlank() && error == null

    open fun showDialog() {
        showDialogState.value = true
    }

    fun onDismissDialog() {
        showDialogState.value = false
        textFiledStateHolder.onValueChange(TextFieldValue("")) { errorState.value = null }
    }
}