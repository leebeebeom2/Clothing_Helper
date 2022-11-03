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
import androidx.compose.ui.unit.dp
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.MaxWidthTextFieldStateHolder
import com.leebeebeom.clothinghelper.base.SimpleIcon
import com.leebeebeom.clothinghelper.base.rememberSubCategoryDialogTextFieldStateHolder
import com.leebeebeom.clothinghelper.main.subcategory.BaseSubCategoryTextFieldDialogStateHolder
import com.leebeebeom.clothinghelper.main.subcategory.SubCategoryTextFieldDialog
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent

@Composable
fun BoxScope.AddCategoryDialogFab(
    onPositiveButtonClick: (String, SubCategoryParent) -> Unit,
    subCategoriesState: List<SubCategory>,
    subCategoryParent: SubCategoryParent
) {
    val state = rememberAddCategoryDialogStateHolder()

    FloatingActionButton(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(end = 16.dp, bottom = 16.dp)
            .size(48.dp),
        onClick = state::showDialog,
        backgroundColor = MaterialTheme.colors.primary,
    ) { SimpleIcon(drawable = R.drawable.ic_add) }

    if (state.showDialog)
        SubCategoryTextFieldDialog(state = state, subCategories = subCategoriesState) {
            onPositiveButtonClick(state.textFiledStateHolder.textState, subCategoryParent)
        }
}

class AddCategoryDialogStateHolder(
    override val titleRes: Int = R.string.add_category,
    override val textFiledStateHolder: MaxWidthTextFieldStateHolder,
    override var showDialogState: MutableState<Boolean>,
    override val errorState: MutableState<Int?>,
) : BaseSubCategoryTextFieldDialogStateHolder()

@Composable
fun rememberAddCategoryDialogStateHolder(
    textFiledStateHolder: MaxWidthTextFieldStateHolder = rememberSubCategoryDialogTextFieldStateHolder(),
    errorState: MutableState<Int?> = rememberSaveable { mutableStateOf(null) },
    showDialogState: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) }
) = remember {
    AddCategoryDialogStateHolder(
        textFiledStateHolder = textFiledStateHolder,
        errorState = errorState,
        showDialogState = showDialogState
    )
}