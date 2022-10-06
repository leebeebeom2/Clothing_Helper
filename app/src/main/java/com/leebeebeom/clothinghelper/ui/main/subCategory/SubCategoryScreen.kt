package com.leebeebeom.clothinghelper.ui.main.subCategory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.data.TextFieldState
import com.leebeebeom.clothinghelper.ui.MaxWidthTextField
import com.leebeebeom.clothinghelper.ui.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.ui.SimpleIcon
import kotlinx.coroutines.delay

@Composable
fun SubCategoryScreen(viewModel: SubCategoryViewModel = viewModel()) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(floatingActionButton = { AddFab { showDialog = true } }) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(viewModel.subCategories) { subCategory -> Text(text = subCategory) }
        }
    }

    if (showDialog) {
        val newCategoryTextFieldState = viewModel.newCategoryTextFieldState

        val onDialogDismissRequest = {
            newCategoryTextFieldState.textInit()
            showDialog = false
        }

        AddCategoryDialog(
            onDismissRequest = onDialogDismissRequest,
            text = newCategoryTextFieldState.text,
            onValueChange = newCategoryTextFieldState.onValueChange,
            isError = newCategoryTextFieldState.isErrorEnabled,
            positiveButtonEnabled = !newCategoryTextFieldState.isBlank && !newCategoryTextFieldState.isErrorEnabled,
            onCancelButtonClick = onDialogDismissRequest,
            onPositiveButtonClick = {
                viewModel.subCategories.add(newCategoryTextFieldState.text)
                onDialogDismissRequest()
            })
    }
}

@Composable
fun AddFab(fabClick: () -> Unit) {
    FloatingActionButton(
        onClick = fabClick,
        backgroundColor = Color(0xFF121212),
        contentColor = Color.White
    ) {
        SimpleIcon(drawableId = R.drawable.ic_add)
    }
}

@Composable
fun AddCategoryDialog(
    onDismissRequest: () -> Unit,
    text: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    positiveButtonEnabled: Boolean,
    onCancelButtonClick: () -> Unit,
    onPositiveButtonClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        DialogRoot {
            Column {
                DialogTitle()
                SimpleHeightSpacer(dp = 12)
                DialogTextField(
                    text = text,
                    onValueChange = onValueChange,
                    isError = isError
                )
                SimpleHeightSpacer(dp = 12)
                DialogTextButtons(positiveButtonEnabled, onCancelButtonClick, onPositiveButtonClick)
                SimpleHeightSpacer(dp = 12)
            }
        }
    }
}

@Composable
fun DialogTextButtons(
    positiveButtonEnabled: Boolean,
    onCancelButtonClick: () -> Unit,
    onPositiveButtonClick: () -> Unit
) {
    Row {
        val weightModifier = Modifier.weight(1f)

        DialogTextButton(
            modifier = weightModifier,
            textColor = MaterialTheme.colors.error,
            textId = R.string.cancel,
            onClick = onCancelButtonClick
        )
        DialogTextButton(
            modifier = weightModifier,
            textId = R.string.check,
            enable = positiveButtonEnabled,
            onClick = onPositiveButtonClick
        )
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
    TextButton(modifier = modifier.padding(vertical = 8.dp).clip(
        RoundedCornerShape(12.dp)
    ), onClick = onClick, enabled = enable) {
        Text(
            text = stringResource(id = textId),
            style = MaterialTheme.typography.subtitle1,
            color = textColor
        )
    }
}

@Composable
private fun DialogTextField(
    text: String,
    onValueChange: (String) -> Unit,
    isError: Boolean
) {
    val focusRequester = remember { FocusRequester() }

    MaxWidthTextField(
        modifier = Modifier.focusRequester(focusRequester),
        text = text,
        onValueChange = onValueChange,
        labelResId = R.string.category,
        placeHolderResId = R.string.category_place_holder,
        isError = isError,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        textFieldError = TextFieldState.TextFieldError.ERROR_SAME_CATEGORY_NAME
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

@Composable
private fun DialogTitle() {
    Text(
        text = stringResource(R.string.add_category_title),
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier.padding(start = 4.dp)
    )
}

@Composable
private fun DialogRoot(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(top = 20.dp, start = 20.dp, end = 20.dp), content = content
    )
}