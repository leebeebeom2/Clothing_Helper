package com.leebeebeom.clothinghelper.main.subcategory

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.MaxWidthTextField
import com.leebeebeom.clothinghelper.base.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.base.SimpleIcon
import com.leebeebeom.clothinghelperdomain.model.SubCategory

@Composable
fun AddCategoryDialogFab(
    modifier: Modifier, onPositiveButtonClick: (String) -> Unit, subCategories: List<SubCategory>
) {
    val state = rememberAddCategoryDialogUIState()

    FloatingActionButton(
        modifier = modifier.size(48.dp),
        onClick = state::showDialog,
        backgroundColor = MaterialTheme.colors.primary,
    ) {
        SimpleIcon(drawable = R.drawable.ic_add)
    }

    if (state.showDialog) Dialog(onDismissRequest = state::onDismissDialog) {
        Surface(color = MaterialTheme.colors.surface, shape = RoundedCornerShape(20.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp)
            ) {
                Text(
                    text = stringResource(R.string.add_category_title),
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
                )
                DialogTextField(categoryName = state.categoryName,
                    error = state.categoryNameError,
                    onCategoryNameChange = { state.onCategoryNameChange(it, subCategories) })
                DialogTextButtons(positiveButtonEnabled = state.positiveButtonEnabled,
                    onCancelButtonClick = state::onDismissDialog,
                    onPositiveButtonClick = {
                        onPositiveButtonClick(state.categoryName)
                        state.onDismissDialog()
                    })
                SimpleHeightSpacer(dp = 20)
            }
        }
    }
}

@Composable
private fun DialogTextButtons(
    positiveButtonEnabled: Boolean,
    onCancelButtonClick: () -> Unit,
    onPositiveButtonClick: () -> Unit
) {
    Row {
        val weightModifier = Modifier.weight(1f)
        DialogTextButton(
            modifier = weightModifier,
            textColor = MaterialTheme.colors.error,
            text = R.string.cancel,
            onClick = onCancelButtonClick
        )
        DialogTextButton(
            modifier = weightModifier,
            text = R.string.check,
            enabled = positiveButtonEnabled,
            onClick = onPositiveButtonClick
        )
    }
}

@Composable
private fun DialogTextButton(
    modifier: Modifier,
    @StringRes text: Int,
    textColor: Color = Color.Unspecified,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Box(modifier = modifier) {
        TextButton(
            modifier = Modifier.align(Alignment.Center),
            onClick = onClick,
            enabled = enabled
        ) {
            Text(
                text = stringResource(id = text),
                style = MaterialTheme.typography.subtitle1,
                color = textColor
            )
        }
    }
}

@Composable
private fun DialogTextField(
    categoryName: String,
    @StringRes error: Int?,
    onCategoryNameChange: (String) -> Unit,
) {
    MaxWidthTextField(
        label = R.string.category,
        placeholder = R.string.category_place_holder,
        text = categoryName,
        onValueChange = onCategoryNameChange,
        error = error,
        showKeyboardEnabled = true
    )
    SimpleHeightSpacer(dp = 12)
}

class AddCategoryDialogUIState(
    categoryName: String = "",
    @StringRes categoryNameError: Int? = null,
    showDialog: Boolean = false
) {
    var categoryName by mutableStateOf(categoryName)
        private set
    var categoryNameError by mutableStateOf(categoryNameError)
        private set
    var showDialog by mutableStateOf(showDialog)
        private set

    fun onCategoryNameChange(newName: String, subCategories: List<SubCategory>) {
        this.categoryName = newName
        categoryNameError = null
        if (subCategories.map { it.name }.contains(newName))
            categoryNameError = R.string.error_same_category_name
    }

    val positiveButtonEnabled get() = categoryName.isNotBlank() && categoryNameError == null

    fun showDialog() {
        showDialog = true
    }

    fun onDismissDialog() {
        showDialog = false
        categoryName = ""
        categoryNameError = null
    }

    companion object {
        val Saver: Saver<AddCategoryDialogUIState, *> = listSaver(save = {
            listOf(
                it.categoryName, it.categoryNameError, it.showDialog
            )
        }, restore = {
            AddCategoryDialogUIState(it[0] as String, it[1] as? Int, it[2] as Boolean)
        })
    }
}

@Composable
fun rememberAddCategoryDialogUIState() = rememberSaveable(saver = AddCategoryDialogUIState.Saver) {
    AddCategoryDialogUIState()
}