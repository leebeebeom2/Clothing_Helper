package com.leebeebeom.clothinghelper.signin.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelperdomain.model.SubCategory

open class BaseSubCategoryTextFieldDialogUIState(
    text: String = "",
    error: Int? = null,
    showDialog: Boolean = false
) {
    var text by mutableStateOf(text)
        protected set
    var error by mutableStateOf(error)
        protected set
    var showDialog by mutableStateOf(showDialog)
        protected set

    fun onTextChange(newName: String, subCategories: List<SubCategory>) {
        text = newName
        error = null
        if (subCategories.map { it.name }.contains(newName))
            error = R.string.error_same_category_name
    }

    val positiveButtonEnabled get() = text.isNotBlank() && error == null

    fun onDismissDialog() {
        showDialog = false
        text = ""
        error = null
    }
}