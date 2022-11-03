package com.leebeebeom.clothinghelper.main.subcategory.content

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.MaxWidthTextFieldStateHolder
import com.leebeebeom.clothinghelper.base.SimpleIcon
import com.leebeebeom.clothinghelper.base.rememberSubCategoryDialogTextFieldStateHolder
import com.leebeebeom.clothinghelper.main.subcategory.BaseSubCategoryTextFieldDialogState
import com.leebeebeom.clothinghelper.main.subcategory.SubCategoryTextFieldDialog
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent

@Composable
fun BoxScope.AddSubcategoryDialogFab(
    onPositiveButtonClick: (String, SubCategoryParent) -> Unit,
    subCategoriesState: List<SubCategory>,
    subCategoryParent: SubCategoryParent
) {
    val state by rememberAddCategoryDialogState(subCategoriesState =  subCategoriesState)

    FloatingActionButton(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(end = 16.dp, bottom = 16.dp)
            .size(48.dp),
        onClick = state::showDialog,
        backgroundColor = MaterialTheme.colors.primary,
    ) { SimpleIcon(drawable = R.drawable.ic_add) }

    if (state.showDialog)
        SubCategoryTextFieldDialog(state = state) {
            onPositiveButtonClick(state.textFiledStateHolder.textState.trim(), subCategoryParent)
        }
}

class AddCategoryDialogState(
    override val titleRes: Int = R.string.add_category,
    override val textFiledStateHolder: MaxWidthTextFieldStateHolder,
    override var showDialogState: MutableState<Boolean>,
    override val errorState: MutableState<Int?>,
    override val subCategoriesState: List<SubCategory>
) : BaseSubCategoryTextFieldDialogState()

@Composable
fun rememberAddCategoryDialogState(
    textFiledStateHolder: MaxWidthTextFieldStateHolder = rememberSubCategoryDialogTextFieldStateHolder(),
    errorState: MutableState<Int?> = rememberSaveable { mutableStateOf(null) },
    showDialogState: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    subCategoriesState: List<SubCategory>
) = remember {
    derivedStateOf {
        AddCategoryDialogState(
            textFiledStateHolder = textFiledStateHolder,
            errorState = errorState,
            showDialogState = showDialogState,
            subCategoriesState = subCategoriesState
        )
    }
}