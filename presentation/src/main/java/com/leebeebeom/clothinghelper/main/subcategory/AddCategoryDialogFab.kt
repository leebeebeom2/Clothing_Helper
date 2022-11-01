package com.leebeebeom.clothinghelper.main.subcategory

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.SimpleIcon
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent

@Composable
fun AddCategoryDialogFab(
    modifier: Modifier,
    onPositiveButtonClick: (String, SubCategoryParent) -> Unit,
    subCategories: List<SubCategory>,
    subCategoryParent: SubCategoryParent
) {
    val state = rememberAddCategoryDialogUIState()

    FloatingActionButton(
        modifier = modifier.size(48.dp),
        onClick = state::showDialog,
        backgroundColor = MaterialTheme.colors.primary,
    ) { SimpleIcon(drawable = R.drawable.ic_add) }


    if (state.showDialog) SubCategoryTextFieldDialog(state = state, onPositiveButtonClick = {
        onPositiveButtonClick(
            state.textFiled.text, subCategoryParent
        )
    })

    LaunchedEffect(key1 = state.textFiled, key2 = subCategories) {
        if (subCategories.map { it.name }
                .contains(state.textFiled.text)) state.updateError(R.string.error_same_category_name)
    }
}

class AddCategoryDialogUIState(
    categoryName: String = "", showDialog: Boolean = false, @StringRes error: Int? = null
) : BaseSubCategoryTextFieldDialogState(
    text = categoryName, showDialog = showDialog, title = R.string.add_category, error = error
) {
    companion object {
        val Saver: Saver<AddCategoryDialogUIState, *> = listSaver(save = {
            listOf(
                it.textFiled.text, it.error, it.showDialog
            )
        }, restore = {
            AddCategoryDialogUIState(it[0] as String, it[1] as Boolean)
        })
    }
}

@Composable
fun rememberAddCategoryDialogUIState() = rememberSaveable(saver = AddCategoryDialogUIState.Saver) {
    AddCategoryDialogUIState()
}