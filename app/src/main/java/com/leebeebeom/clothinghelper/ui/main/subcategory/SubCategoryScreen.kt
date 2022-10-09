package com.leebeebeom.clothinghelper.ui.main.subcategory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.MaxWidthTextField
import com.leebeebeom.clothinghelper.ui.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.ui.SimpleIcon
import com.leebeebeom.clothinghelper.ui.SimpleWidthSpacer
import com.leebeebeom.clothinghelper.ui.base.TextFieldUIState
import kotlinx.coroutines.delay

@Composable
fun SubCategoryScreen(viewModel: SubCategoryViewModel = viewModel()) {
    val dialogState = viewModel.addDialogState

    Scaffold(
        floatingActionButton = {
            AddFab(fabClick = viewModel.onShowAddCategoryDialog)
        }
    ) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(dialogState.subCategories) { subCategory -> Text(text = subCategory) }
        }
    }

    if (dialogState.isDialogShowing) {
        AddCategoryDialog(
            onDismissDialog = viewModel.onDismissAddCategoryDialog,
            categoryTextFieldState = dialogState.categoryTextFieldState,
            onNewCategoryNameChange = viewModel.onNewCategoryNameChange,
            positiveButtonEnabled = dialogState.positiveButtonEnable,
            onCancelButtonClick = viewModel.onDismissAddCategoryDialog,
            onPositiveButtonClick = {
                viewModel.addNewCategory()
                viewModel.onDismissAddCategoryDialog()
            },
        )
    }
}

@Composable
fun AddFab(fabClick: () -> Unit) {
    FloatingActionButton(
        onClick = fabClick,
        backgroundColor = Color.Black,
        contentColor = Color.White
    ) {
        SimpleIcon(drawableId = R.drawable.ic_add, contentDescription = "add icon")
    }
}

@Composable
fun AddCategoryDialog(
    onDismissDialog: () -> Unit,
    categoryTextFieldState: TextFieldUIState,
    onNewCategoryNameChange: (String) -> Unit,
    positiveButtonEnabled: Boolean,
    onCancelButtonClick: () -> Unit,
    onPositiveButtonClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismissDialog) {
        DialogRoot {
            Column {
                DialogTitle()
                SimpleHeightSpacer(dp = 12)
                DialogTextField(
                    categoryTextFieldState = categoryTextFieldState,
                    onNewCategoryNameChange = onNewCategoryNameChange
                )
                SimpleHeightSpacer(dp = 12)
                DialogTextButtons(
                    positiveButtonEnabled = positiveButtonEnabled,
                    onCancelButtonClick = onCancelButtonClick,
                    onPositiveButtonClick = onPositiveButtonClick
                )
                SimpleHeightSpacer(dp = 12)
            }
        }
    }
}

@Composable
private fun DialogRoot(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(top = 20.dp, start = 20.dp, end = 20.dp),
        content = content
    )
}

@Composable
private fun DialogTitle() =
    Text(
        text = stringResource(R.string.add_category_title),
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier.padding(start = 4.dp)
    )

@Composable
fun DialogTextButtons(
    positiveButtonEnabled: Boolean,
    onCancelButtonClick: () -> Unit,
    onPositiveButtonClick: () -> Unit
) {
    Row {
        val weightModifier = Modifier.weight(1f)
        SimpleWidthSpacer(dp = 20)
        DialogTextButton(
            modifier = weightModifier,
            textColor = MaterialTheme.colors.error,
            textId = R.string.cancel,
            onClick = onCancelButtonClick
        )
        SimpleWidthSpacer(dp = 20)
        DialogTextButton(
            modifier = weightModifier,
            textId = R.string.check,
            enable = positiveButtonEnabled,
            onClick = onPositiveButtonClick
        )
        SimpleWidthSpacer(dp = 20)
    }
}

@Composable
private fun DialogTextButton(
    modifier: Modifier,
    textId: Int,
    textColor: Color = Color.Unspecified,
    enable: Boolean = true,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enable,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = stringResource(id = textId),
            style = MaterialTheme.typography.subtitle1,
            color = textColor
        )
    }
}

@Composable
private fun DialogTextField(
    categoryTextFieldState: TextFieldUIState,
    onNewCategoryNameChange: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    MaxWidthTextField(
        modifier = Modifier.focusRequester(focusRequester),
        textFieldUIState = categoryTextFieldState,
        onValueChange = onNewCategoryNameChange,
        labelResId = R.string.category,
        placeHolderResId = R.string.category_place_holder,
    )

    ShowKeyboard(focusRequester)
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun ShowKeyboard(focusRequester: FocusRequester) {
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        delay(100)
        keyboardController?.show()
    }
}