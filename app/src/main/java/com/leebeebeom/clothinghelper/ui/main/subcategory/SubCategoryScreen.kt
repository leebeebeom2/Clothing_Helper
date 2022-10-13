package com.leebeebeom.clothinghelper.ui.main.subcategory

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.OnClick
import com.leebeebeom.clothinghelper.ui.ClickableIcon
import com.leebeebeom.clothinghelper.ui.MaxWidthTextField
import com.leebeebeom.clothinghelper.ui.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.ui.SimpleIcon
import com.leebeebeom.clothinghelper.ui.base.TextFieldUIState
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SubCategoryScreenPreview() {
    ClothingHelperTheme {
        SubCategoryScreen()
    }
}

@Composable
fun SubCategoryScreen(viewModel: SubCategoryViewModel = viewModel()) {
    val state = viewModel.subCategoryUIState

    Scaffold(floatingActionButton = { AddFab(fabClick = viewModel.showAddCategoryDialog) }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.subCategories) {
                SubCategoryContent(it)
            }
        }
    }

    if (state.showDialog) {
        AddCategoryDialog(
            onDismissDialog = viewModel.onDismissAddCategoryDialog,
            categoryTextFieldState = state.categoryName,
            onNewCategoryNameChange = viewModel.onNewCategoryNameChange,
            positiveButtonEnabled = state.positiveButtonEnabled,
            onCancelButtonClick = viewModel.onDismissAddCategoryDialog,
            onPositiveButtonClick = {
                viewModel.addNewCategory()
                viewModel.onDismissAddCategoryDialog()
            },
        )
    }
}

@Composable
private fun SubCategoryContent(title: String) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(), elevation = 2.dp, shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            SubCategoryTitle(title, isExpanded) { isExpanded = !isExpanded }
            SubCategoryInfo(isExpanded)
        }
    }
}

@Composable
private fun SubCategoryInfo(isExpanded: Boolean) {
    AnimatedVisibility(
        visible = isExpanded, enter = expandVertically(), exit = shrinkVertically()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.background)
                .padding(8.dp)
        ) {
            val weightModifier = Modifier.weight(1f)

            SubCategoryInfoText(
                modifier = weightModifier,
                infoTitle = R.string.average_size,
                info = R.string.top_info
            )

            SubCategoryInfoText(
                modifier = weightModifier,
                infoTitle = R.string.most_have_size,
                info = R.string.top_info
            )
        }
    }
}

@Composable
private fun SubCategoryInfoText(
    modifier: Modifier, @StringRes infoTitle: Int, @StringRes info: Int
) {
    ProvideTextStyle(
        value = MaterialTheme.typography.caption.copy(
            color = LocalContentColor.current.copy(ContentAlpha.medium), fontSize = 13.sp
        )
    ) {
        Column(modifier = modifier.padding(start = 8.dp)) {
            Text(
                text = stringResource(id = infoTitle), fontWeight = FontWeight.Bold
            )
            SimpleHeightSpacer(dp = 2)
            Text(text = stringResource(id = info))
        }
    }
}


@Composable
private fun SubCategoryTitle(title: String, isExpanded: Boolean, onExpandIconClick: OnClick) {
    Box(modifier = Modifier
        .fillMaxSize()
        .clickable { }
        .padding(start = 12.dp)
        .padding(vertical = 4.dp)) {
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            text = title,
            style = MaterialTheme.typography.subtitle1
        )
        ExpandIcon(Modifier.align(Alignment.TopEnd), isExpanded, onExpandIconClick)
    }
}

@Composable
private fun ExpandIcon(modifier: Modifier, isExpanded: Boolean, onExpandIconClick: OnClick) {
    val rotate by animateFloatAsState(
        targetValue = if (!isExpanded) 0f else 180f, animationSpec = tween(durationMillis = 300)
    )

    ClickableIcon(
        drawable = R.drawable.ic_expand_more,
        modifier = modifier.rotate(rotate),
        tint = LocalContentColor.current.copy(ContentAlpha.medium),
        onClick = onExpandIconClick
    )
}

@Composable
fun AddFab(fabClick: () -> Unit) {
    FloatingActionButton(
        modifier = Modifier.size(44.dp),
        onClick = fabClick,
        backgroundColor = MaterialTheme.colors.primary,
    ) {
        SimpleIcon(drawable = R.drawable.ic_add, contentDescription = "add icon")
    }
}

@Composable
private fun AddCategoryDialog(
    onDismissDialog: () -> Unit,
    categoryTextFieldState: TextFieldUIState,
    onNewCategoryNameChange: (String) -> Unit,
    positiveButtonEnabled: Boolean,
    onCancelButtonClick: () -> Unit,
    onPositiveButtonClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismissDialog) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colors.surface)
                .padding(20.dp)
        ) {
            Text(
                text = stringResource(R.string.add_category_title),
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
            )
            DialogTextField(
                categoryTextFieldState = categoryTextFieldState,
                onNewCategoryNameChange = onNewCategoryNameChange
            )
            DialogTextButtons(
                positiveButtonEnabled = positiveButtonEnabled,
                onCancelButtonClick = onCancelButtonClick,
                onPositiveButtonClick = onPositiveButtonClick
            )
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
            enabled = enabled,
            shape = RoundedCornerShape(12.dp)
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
    categoryTextFieldState: TextFieldUIState,
    onNewCategoryNameChange: (String) -> Unit,
) {
    MaxWidthTextField(
        textFieldState = categoryTextFieldState,
        onValueChange = onNewCategoryNameChange,
        label = R.string.category,
        placeHolder = R.string.category_place_holder,
        showKeyboardEnabled = true
    )
    SimpleHeightSpacer(dp = 12)
}