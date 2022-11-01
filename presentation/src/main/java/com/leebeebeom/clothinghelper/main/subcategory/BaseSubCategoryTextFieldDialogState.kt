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
    text: String = "", showDialog: Boolean = false, @StringRes val title: Int
) : MaxWidthTextFieldState(
    label = R.string.category,
    placeholder = R.string.category_place_holder,
    text = text,
    showKeyboardEnabled = true
) {
    var showDialog by mutableStateOf(showDialog)
        private set

    fun onCategoryNameChange(newText: TextFieldValue, subCategories: List<SubCategory>) {
        super.onValueChange(newText)
        if (subCategories.map { it.name }.contains(newText.text))
            updateError(R.string.error_same_category_name)
    }

    val positiveButtonEnabled get() = textFiled.text.isNotBlank() && error == null

    fun showDialog() {
        showDialog = true
    }

    fun onDismissDialog() {
        showDialog = false
        onValueChange(TextFieldValue(""))
    }
}