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
            positiveButtonEnabled = state.positiveButtonEnabled,
            onValueChange = state::onValueChange,
            onFocusChanged = state::onFocusChange,
            onDismiss = state::onDismiss,
            onPositiveButtonClick = { onPositiveButtonClick(state.text) }
        )
}

data class AddCategoryDialogState(
    private val textState: MutableState<String>,
    private val textFieldValueState: MutableState<TextFieldValue>,
    private val showDialogState: MutableState<Boolean>,
    private val errorState: MutableState<Int?>,
    val subCategories: List<SubCategory>
) {
    val showDialog get() = showDialogState.value
    val error get() = errorState.value
    val text get() = textState.value

    val positiveButtonEnabled get() = text.isNotBlank() && error == null

    fun showDialog() {
        showDialogState.value = true
    }

    fun onDismiss() {
        showDialogState.value = false
        textState.value = ""
        textFieldValueState.value = TextFieldValue("")
        errorState.value = null
    }

    fun onValueChange(newTextFiled: TextFieldValue) {
        if (text != newTextFiled.text) errorState.value = null
        textState.value = newTextFiled.text
        textFieldValueState.value = newTextFiled
        if (subCategories.map { it.name }.contains(newTextFiled.text))
            errorState.value = R.string.error_same_category_name
    }

    fun onFocusChange(newFocusState: FocusState) {
        if (newFocusState.hasFocus)
            textFieldValueState.value =
                textFieldValueState.value.copy(selection = TextRange(textFieldValueState.value.text.length))
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
        textFieldValueState = textFieldValueState,
        errorState = errorState,
        showDialogState = showDialogState,
        subCategories = subCategories
    )
}