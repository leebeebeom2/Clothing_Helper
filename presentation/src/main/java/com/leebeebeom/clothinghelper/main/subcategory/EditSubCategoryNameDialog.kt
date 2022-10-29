package com.leebeebeom.clothinghelper.main.subcategory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelperdomain.model.SubCategory

@Composable
fun EditSubCategoryNameDialog(
    initialCategoryName: String,
    subCategories: List<SubCategory>,
    onPositiveButtonClick: (String) -> Unit,
    onDismissDialog: () -> Unit
) {
    // TODO 커서 포지션(텍스트 셀렉트),  처음 다이얼로그 보일 시 확인 버튼 disable(변경되어야 enable)
    val state = rememberEditSubCategoryNameDialogUIState(initialCategoryName)

    SubCategoryTextFieldDialog(
        categoryName = state.categoryName,
        error = state.error,
        onDismissDialog = onDismissDialog,
        title = R.string.edit_category_name,
        onCategoryNameChange = {
            state.onCategoryNameChange(
                newName = it,
                subCategories = subCategories
            )
        },
        positiveButtonEnabled = state.editPositiveButtonEnabled(subCategories),
        onPositiveButtonClick = { onPositiveButtonClick(state.categoryName.text) },
        onFocusChanged = state.onCategoryNameFocusChanged
    )
}

class EditSubCategoryNameDialogUIState(
    initialCategoryName: String = "",
    error: Int? = null,
    showDialog: Boolean = false // 미사용
) : BaseSubCategoryTextFieldDialogUIState(initialCategoryName, error, showDialog) {

    override val text: MutableState<TextFieldValue> =
        mutableStateOf(TextFieldValue(initialCategoryName))

    val onCategoryNameFocusChanged = { focusState: FocusState ->
        if (focusState.hasFocus)
            this.text.value = this.text.value.copy(
                selection = TextRange(0, text.value.text.length)
            )
    }

    fun editPositiveButtonEnabled(subCategories: List<SubCategory>) =
        super.positiveButtonEnabled && !subCategories.map { it.name }.contains(categoryName.text)

    companion object {
        val Saver: Saver<EditSubCategoryNameDialogUIState, *> = listSaver(
            save = {
                listOf(
                    it.categoryName.text,
                    it.error,
                    it.showDialog
                )
            },
            restore = {
                EditSubCategoryNameDialogUIState(
                    it[0] as String, it[1] as? Int, it[2] as Boolean
                )
            }
        )
    }
}

@Composable
fun rememberEditSubCategoryNameDialogUIState(initialCategoryName: String) =
    rememberSaveable(saver = EditSubCategoryNameDialogUIState.Saver) {
        EditSubCategoryNameDialogUIState(initialCategoryName)
    }