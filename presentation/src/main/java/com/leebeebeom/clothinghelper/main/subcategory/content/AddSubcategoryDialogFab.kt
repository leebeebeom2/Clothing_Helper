package com.leebeebeom.clothinghelper.main.subcategory.content

import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.SimpleIcon
import com.leebeebeom.clothinghelper.main.subcategory.BaseSubCategoryDialogState
import com.leebeebeom.clothinghelper.main.subcategory.SubCategoryTextFieldDialog
import com.leebeebeom.clothinghelperdomain.model.SubCategory

@Composable
fun BoxScope.AddSubcategoryDialogFab(
    onPositiveButtonClick: (String) -> Unit,
    subCategories: () -> List<SubCategory>,
    paddingValues: () -> PaddingValues
) {
    val state =
        rememberSaveable(saver = AddSubCategoryDialogState.Saver) { AddSubCategoryDialogState() }

    AddSubCategoryFab(paddingValues = paddingValues, showDialog = state::showDialog)

    val subCategoryNames by remember { derivedStateOf { subCategories().map { it.name } } }
    SubCategoryTextFieldDialog(
        title = R.string.add_category,
        error = { state.error },
        textFieldValue = { state.textFieldValue },
        positiveButtonEnabled = { state.positiveButtonEnabled },
        showDialog = { state.showDialog },
        onValueChange = {
            state.onValueChange(it)
            if (subCategoryNames.contains(it.text.trim())) state.updateError(R.string.error_same_category_name)
        },
        onFocusChanged = state::onFocusChange,
        onDismiss = state::onDismiss,
        onPositiveButtonClick = { onPositiveButtonClick(state.text.trim()) }
    )
}

class AddSubCategoryDialogState(
    initialText: String = "",
    initialError: Int? = null,
    initialShowDialog: Boolean = false,
) : BaseSubCategoryDialogState(initialText, initialError) {
    var showDialog by mutableStateOf(initialShowDialog)
        private set

    fun showDialog() {
        showDialog = true
    }

    fun onDismiss() {
        showDialog = false
        text = ""
        textFieldValue = TextFieldValue("")
        error = null
    }

    fun onFocusChange(newFocusState: FocusState) {
        if (newFocusState.hasFocus)
            textFieldValue = textFieldValue.copy(selection = TextRange(text.length))
    }

    companion object {
        val Saver: Saver<AddSubCategoryDialogState, *> = listSaver(
            save = { listOf(it.text, it.error, it.showDialog) },
            restore = {
                AddSubCategoryDialogState(
                    it[0] as String,
                    it[1] as? Int,
                    it[2] as Boolean
                )
            }
        )
    }
}

@Composable
fun BoxScope.AddSubCategoryFab(
    showDialog: () -> Unit,
    paddingValues: () -> PaddingValues
) {
    FloatingActionButton(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(end = 16.dp, bottom = 16.dp)
            .padding(paddingValues())
            .size(48.dp),
        onClick = showDialog,
        backgroundColor = MaterialTheme.colors.primary,
    ) { SimpleIcon(drawable = R.drawable.ic_add) }
}