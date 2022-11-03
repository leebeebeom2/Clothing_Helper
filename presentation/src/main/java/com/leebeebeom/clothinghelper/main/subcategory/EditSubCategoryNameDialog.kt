package com.leebeebeom.clothinghelper.main.subcategory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.TextRange
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.MaxWidthTextFieldStateHolder
import com.leebeebeom.clothinghelper.base.rememberSubCategoryDialogTextFieldStateHolder
import com.leebeebeom.clothinghelperdomain.model.SubCategory

@Composable
fun EditSubCategoryNameDialog(
    getInitialName: () -> String,
    subCategoriesState: List<SubCategory>,
    onPositiveButtonClick: (String) -> Unit,
) {
    val state = rememberEditSubCategoryNameDialogState(
        initialName = getInitialName(),
        subCategoriesState = subCategoriesState
    )
    SubCategoryTextFieldDialog(state = state) { onPositiveButtonClick(state.textFiledStateHolder.textState.trim()) }
}

data class EditSubCategoryNameDialogState(
    val initialName: String,
    override val titleRes: Int = R.string.edit_category_name,
    override val textFiledStateHolder: MaxWidthTextFieldStateHolder,
    override val errorState: MutableState<Int?>,
    override var showDialogState: MutableState<Boolean>,
    override val subCategoriesState: List<SubCategory>
) : BaseSubCategoryTextFieldDialogState() {

    init {
        textFiledStateHolder.onFocusChanged = {
            if (it.hasFocus)
                textFiledStateHolder.onValueChange(
                    textFiledStateHolder.textFieldState.copy(
                        selection = TextRange(
                            0, textFiledStateHolder.textState.length
                        )
                    )
                ) {}
        }
    }

    override fun updateError(newName: String) {
        super.updateError(newName)
        if (newName.trim() == initialName) errorState.value = null
    }

    override val positiveButtonEnabled: Boolean
        get() = super.positiveButtonEnabled && initialName != textFiledStateHolder.textState.trim()
}

@Composable
fun rememberEditSubCategoryNameDialogState(
    initialName: String,
    textFiledStateHolder: MaxWidthTextFieldStateHolder = rememberSubCategoryDialogTextFieldStateHolder(
        initialName
    ),
    errorState: MutableState<Int?> = rememberSaveable { mutableStateOf(null) },
    showDialogState: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    subCategoriesState: List<SubCategory>
) = remember(subCategoriesState) {
    EditSubCategoryNameDialogState(
        initialName = initialName,
        textFiledStateHolder = textFiledStateHolder,
        errorState = errorState,
        showDialogState = showDialogState,
        subCategoriesState = subCategoriesState
    )
}