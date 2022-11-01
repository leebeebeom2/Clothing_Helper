package com.leebeebeom.clothinghelper.main.subcategory

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.MaxWidthTextFieldState
import com.leebeebeom.clothinghelperdomain.model.SubCategory

open class BaseSubCategoryTextFieldDialogState(
    text: String = "",
    error: Int? = null,
    showDialog: Boolean = false,
    @StringRes val title: Int
) : MaxWidthTextFieldState(
    label = R.string.category,
    placeholder = R.string.category_place_holder,
    text = text,
    showKeyboardEnabled = true,
    error = error
) {
    var showDialog by mutableStateOf(showDialog)
        private set

    open val positiveButtonEnabled get() = textFiled.text.isNotBlank() && error == null

    fun showDialog() {
        showDialog = true
    }

    fun onDismissDialog() {
        showDialog = false
        onValueChange(TextFieldValue(""))
    }
}