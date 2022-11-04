package com.leebeebeom.clothinghelper.main.subcategory.content

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.SimpleIcon
import com.leebeebeom.clothinghelper.main.subcategory.BaseSubCategoryTextFieldDialogState
import com.leebeebeom.clothinghelper.main.subcategory.SubCategoryTextFieldDialog
import com.leebeebeom.clothinghelperdomain.model.SubCategory

@Composable
fun BoxScope.AddSubcategoryDialogFab(
    onPositiveButtonClick: (String) -> Unit,
    subCategories: List<SubCategory>,
) {
    val state = rememberAddCategoryDialogState(subCategories = subCategories)

    FloatingActionButton(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(end = 16.dp, bottom = 16.dp)
            .size(48.dp),
        onClick = state::showDialog,
        backgroundColor = MaterialTheme.colors.primary,
    ) { SimpleIcon(drawable = R.drawable.ic_add) }

    if (state.showDialog)
        SubCategoryTextFieldDialog(
            titleRes = R.string.add_category,
            error = state.error,
            textFieldValueState = state.textFieldValueState,
            positiveButtonEnabled = state.positiveButtonEnabled,
            onValueChange = state::onValueChange,
            onFocusChanged = state::onFocusChange,
            onDismiss = state::onDismiss,
            onPositiveButtonClick = { onPositiveButtonClick(state.text) }
        )
}

data class AddCategoryDialogState(
    override val textState: MutableState<String>,
    override val textFieldValueMutableState: MutableState<TextFieldValue>,
    private val showDialogState: MutableState<Boolean>,
    override val errorState: MutableState<Int?>,
    override val subCategories: List<SubCategory>
) : BaseSubCategoryTextFieldDialogState() {
    val showDialog get() = showDialogState.value

    fun showDialog() {
        showDialogState.value = true
    }

    fun onDismiss() {
        showDialogState.value = false
        textState.value = ""
        textFieldValueMutableState.value = TextFieldValue("")
        errorState.value = null
    }

    fun onFocusChange(newFocusState: FocusState) {
        if (newFocusState.hasFocus)
            textFieldValueMutableState.value =
                textFieldValueMutableState.value.copy(
                    selection = TextRange(
                        textFieldValueMutableState.value.text.length
                    )
                )
    }
}

@Composable
fun rememberAddCategoryDialogState(
    textState: MutableState<String> = rememberSaveable { mutableStateOf("") },
    textFieldValueState: MutableState<TextFieldValue> = remember {
        mutableStateOf(
            TextFieldValue(
                textState.value
            )
        )
    },
    errorState: MutableState<Int?> = rememberSaveable { mutableStateOf(null) },
    showDialogState: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    subCategories: List<SubCategory>
) = remember(subCategories) {
    AddCategoryDialogState(
        textState = textState,
        textFieldValueMutableState = textFieldValueState,
        errorState = errorState,
        showDialogState = showDialogState,
        subCategories = subCategories
    )
}