package com.leebeebeom.clothinghelper.main.subcategory

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.signin.base.OneTextFiledState
import com.leebeebeom.clothinghelperdomain.model.SubCategory

open class BaseSubCategoryTextFieldDialogUIState(
    text: String = "", error: Int? = null, showDialog: Boolean = false
) : OneTextFiledState(text) {
    open val categoryName get() = text.value

    var error by mutableStateOf(error)
        protected set
    var showDialog by mutableStateOf(showDialog)
        protected set

    private val hideCategoryNameError = { this.error = null }

    private val categoryNameInit = { this.text.value = "" }

    fun setCategoryName(categoryName: String) {
        text.value = categoryName
    }

    fun onCategoryNameChange(newName: String, subCategories: List<SubCategory>) {
        super.onTextChange(newName, hideCategoryNameError)
        if (subCategories.map { it.name }.contains(newName)) error =
            R.string.error_same_category_name
    }

    val positiveButtonEnabled get() = categoryName.isNotBlank() && error == null

    val onDismissDialog = {
        this.showDialog = false
        categoryNameInit()
        this.error = null
    }
}