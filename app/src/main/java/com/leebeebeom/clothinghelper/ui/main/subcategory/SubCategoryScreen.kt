package com.leebeebeom.clothinghelper.ui.main.subcategory

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.ui.SimpleIcon
import com.leebeebeom.clothinghelper.ui.base.MaxWidthTextField
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SubCategoryScreenPreview() {
    ClothingHelperTheme {
        SubCategoryScreen()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SubCategoryScreen(viewModel: SubCategoryViewModel = viewModel()) {
    val viewModelState = viewModel.viewModelState

    Box(modifier = Modifier.fillMaxSize()) {
        var cell by rememberSaveable { mutableStateOf(1) }

        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            columns = GridCells.Fixed(cell)
        ) {
            items(viewModelState.subCategories, key = { it }) {
                val modifier = Modifier.animateItemPlacement(animationSpec = tween(425))
                SubCategoryContent(modifier, it)
            }
            item {
                Button(onClick = { cell = if (cell == 1) 2 else 1 }) {

                }
            }
        }

        AddCategoryDialogFab(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp),
            onPositiveButtonClick = viewModelState::addNewCategory,
            subCategories = viewModelState.subCategories
        )
    }
}

@Composable
private fun SubCategoryContent(modifier: Modifier, title: String) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Card(modifier = modifier, elevation = 2.dp, shape = RoundedCornerShape(12.dp)) {
        Column {
            SubCategoryTitle(title, isExpanded) { isExpanded = !isExpanded }
            SubCategoryInfo(isExpanded)
        }
    }
}

@Composable
private fun SubCategoryInfo(isExpanded: Boolean) {
    AnimatedVisibility(
        visible = isExpanded,
        enter = expandVertically(animationSpec = tween(250)),
        exit = shrinkVertically(animationSpec = tween(250))
    ) {
        Surface(color = MaterialTheme.colors.background) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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
private fun SubCategoryTitle(title: String, isExpanded: Boolean, onExpandIconClick: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxSize()
        .clickable { /*TODO*/ }
        .padding(start = 12.dp)
        .padding(vertical = 4.dp)) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f),
            text = title,
            style = MaterialTheme.typography.subtitle1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        ExpandIcon(isExpanded, onExpandIconClick)
    }
}

@Composable
private fun ExpandIcon(isExpanded: Boolean, onExpandIconClick: () -> Unit) {
    val rotate by animateFloatAsState(
        targetValue = if (!isExpanded) 0f else 180f, animationSpec = tween(durationMillis = 300)
    )

    IconButton(onClick = onExpandIconClick) {
        SimpleIcon(
            drawable = R.drawable.ic_expand_more,
            modifier = Modifier.rotate(rotate),
            tint = LocalContentColor.current.copy(ContentAlpha.medium)
        )
    }
}

@Composable
private fun AddCategoryDialogFab(
    modifier: Modifier,
    onPositiveButtonClick: (String) -> Unit,
    subCategories: List<String>
) {
    val state = rememberAddCategoryDialogUIState()

    FloatingActionButton(
        modifier = modifier.size(48.dp),
        onClick = state::showDialog,
        backgroundColor = MaterialTheme.colors.primary,
    ) {
        SimpleIcon(drawable = R.drawable.ic_add, contentDescription = "add icon")
    }

    if (state.showDialog)
        Dialog(onDismissRequest = state::dismissDialog) {
            Surface(color = MaterialTheme.colors.surface, shape = RoundedCornerShape(20.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.add_category_title),
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
                    )
                    DialogTextField(
                        categoryName = state.categoryName,
                        error = state.categoryNameError,
                        onCategoryNameChange = {
                            state.onCategoryNameChange(it)
                            if (subCategories.contains(it))
                                state.enableCategoryNameError(R.string.error_same_category_name)
                        }
                    )
                    DialogTextButtons(
                        positiveButtonEnabled = state.positiveButtonEnabled,
                        onCancelButtonClick = state::dismissDialog,
                        onPositiveButtonClick = {
                            state.dismissDialog()
                            onPositiveButtonClick(state.categoryName)
                        })
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
            enabled = enabled,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = stringResource(id = text),
                style = MaterialTheme.typography.subtitle1,
                color = textColor
            )
            SimpleHeightSpacer(dp = 40)
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
    initialCategoryName: String = "",
    @StringRes initialCategoryNameError: Int? = null,
    initialShowDialog: Boolean = false
) {
    var categoryName by mutableStateOf(initialCategoryName)
        private set
    var categoryNameError by mutableStateOf(initialCategoryNameError)
        private set
    var showDialog by mutableStateOf(initialShowDialog)
        private set

    fun onCategoryNameChange(categoryName: String) {
        this.categoryName = categoryName
        categoryNameError = null
    }

    fun enableCategoryNameError(@StringRes error: Int) {
        categoryNameError = error
    }

    val positiveButtonEnabled get() = categoryName.isNotBlank() && categoryNameError == null

    fun showDialog() {
        showDialog = true
    }

    fun dismissDialog() {
        showDialog = false
        categoryName = ""
        categoryNameError = null
    }

    companion object {
        val Saver: Saver<AddCategoryDialogUIState, *> = listSaver(
            save = {
                listOf(
                    it.categoryName,
                    it.categoryNameError,
                    it.showDialog
                )
            },
            restore = {
                AddCategoryDialogUIState(it[0] as String, it[1] as? Int, it[2] as Boolean)
            }
        )
    }
}

@Composable
fun rememberAddCategoryDialogUIState() =
    rememberSaveable(saver = AddCategoryDialogUIState.Saver) {
        AddCategoryDialogUIState()
    }