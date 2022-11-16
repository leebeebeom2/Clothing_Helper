package com.leebeebeom.clothinghelper.main.subcategory.content

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.SimpleIcon
import com.leebeebeom.clothinghelper.main.base.AddSubCategoryDialog
import com.leebeebeom.clothinghelper.main.base.Fab
import com.leebeebeom.clothinghelper.main.subcategory.BaseSubCategoryDialogState
import kotlinx.collections.immutable.ImmutableList

@Composable
fun BoxScope.AddSubcategoryDialogFab(
    onPositiveButtonClick: (newName: String) -> Unit,
    subCategoryNames: () -> ImmutableList<String>,
    isSelectMode: () -> Boolean,
) {
    val state =
        rememberSaveable(saver = AddSubCategoryDialogState.Saver) { AddSubCategoryDialogState() }

    AddSubCategoryFab(isSelectMode = isSelectMode, showDialog = state::showDialog)

    AddSubCategoryDialog(state = state,
        subCategoryNames = subCategoryNames,
        onPositiveButtonClick = { onPositiveButtonClick(state.text) })
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
        if (newFocusState.hasFocus) textFieldValue =
            textFieldValue.copy(selection = TextRange(text.length))
    }

    companion object {
        val Saver: Saver<AddSubCategoryDialogState, *> =
            listSaver(save = { listOf(it.text, it.error, it.showDialog) }, restore = {
                AddSubCategoryDialogState(
                    it[0] as String, it[1] as? Int, it[2] as Boolean
                )
            })
    }
}

@Composable
fun BoxScope.AddSubCategoryFab(
    showDialog: () -> Unit, isSelectMode: () -> Boolean
) {
    Fab(
        visible = { !isSelectMode() }, onClick = showDialog
    ) { SimpleIcon(drawable = R.drawable.ic_add) }
}