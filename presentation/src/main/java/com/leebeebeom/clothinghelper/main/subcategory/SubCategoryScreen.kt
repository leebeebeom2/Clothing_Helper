package com.leebeebeom.clothinghelper.main.subcategory

import androidx.activity.compose.BackHandler
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent


// TODO 다이얼로그 애니메이션

@Composable
fun SubCategoryScreen(
    mainCategoryName: String,
    viewModel: SubCategoryViewModel = hiltViewModel(),
    isSubCategoriesLoading: Boolean
) {
    val viewModelState = viewModel.viewModelState
    val state = rememberSubCategoryScreenUIState(mainCategoryName = mainCategoryName)
    val editSubCategoryNameDialogUIState = rememberEditSubCategoryNameDialogUIState()

    Scaffold(bottomBar = {
        SubCategoryBottomAppBar(
            isSelectMode = state.selectMode,
            selectedSubCategoriesSize = state.selectedSubCategories.size,
            subCategoriesSize = viewModelState.getSubCategories(state.subCategoryParent).size,
            onAllSelectCheckBoxClick = { state.toggleAllSelect(viewModelState.getSubCategories(state.subCategoryParent)) },
            onEditSubCategoryNameClick = {
                editSubCategoryNameDialogUIState.showDialog(state.selectedSubCategories.first().name)
                state.selectModeOff()
            }
        )
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (isSubCategoriesLoading)
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = LocalContentColor.current.copy(ContentAlpha.medium)
                )
            // TODO 이름 수정
            else SubCategoryContent(
                subCategoryParent = state.subCategoryParent,
                allExpandIconClick = viewModel::toggleAllExpand,
                allExpand = viewModelState.allExpand,
                subCategories = viewModelState.getSubCategories(state.subCategoryParent),
                onLongClick = state.selectModeOn,
                isSelectMode = state.selectMode,
                onSelect = state.onSelect,
                selectedSubCategories = state.selectedSubCategories
            )

            AddCategoryDialogFab(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 16.dp),
                onPositiveButtonClick = viewModel.addSubCategory,
                subCategories = viewModelState.getSubCategories(state.subCategoryParent)
            )
        }
    }

    if (editSubCategoryNameDialogUIState.showDialog)
        EditSubCategoryNameDialog(
            categoryName = editSubCategoryNameDialogUIState.text,
            error = editSubCategoryNameDialogUIState.error,
            onCategoryNameChange = {
                editSubCategoryNameDialogUIState.onTextChange(
                    it,
                    viewModelState.getSubCategories(state.subCategoryParent)
                )
            },
            onPositiveButtonClick = { /*TODO*/ },
            onDismissDialog = editSubCategoryNameDialogUIState::onDismissDialog,
            positiveButtonEnabled = editSubCategoryNameDialogUIState.positiveButtonEnabled
        )

    BackHandler(enabled = state.selectMode, onBack = state.selectModeOff)
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun CircleCheckBox(modifier: Modifier = Modifier, isChecked: Boolean) {
    Icon(
        modifier = modifier,
        painter = rememberAnimatedVectorPainter(
            animatedImageVector = AnimatedImageVector.animatedVectorResource(
                id = R.drawable.check_anim
            ), atEnd = isChecked
        ),
        contentDescription = null,
        tint = LocalContentColor.current.copy(0.7f)
    )
}

class SubCategoryScreenUIState(
    mainCategoryName: String,
    selectMode: Boolean = false,
    vararg selectedSubCategories: SubCategory = emptyArray()
) {
    val subCategoryParent = enumValueOf<SubCategoryParent>(mainCategoryName)

    var selectMode by mutableStateOf(selectMode)
        private set

    val selectModeOff = { this.selectMode = false }

    val selectModeOn = { this.selectMode = true }

    val onSelect = { subCategory: SubCategory ->
        this.selectedSubCategories =
            if (this.selectedSubCategories.contains(subCategory))
                this.selectedSubCategories.taskAndReturn { it.remove(subCategory) }
            else this.selectedSubCategories.taskAndReturn { it.add(subCategory) }
    }

    var selectedSubCategories by mutableStateOf(selectedSubCategories.toSet()) // TODO remember
        private set

    fun toggleAllSelect(subCategories: List<SubCategory>) {
        selectedSubCategories =
            if (selectedSubCategories.size == subCategories.size) emptySet() else subCategories.toSet()
    }

    companion object {
        val Saver: Saver<SubCategoryScreenUIState, *> = listSaver(
            save = { listOf(it.subCategoryParent.name, it.selectMode, it.selectedSubCategories) },
            restore = {
                val selectedSubCategories = it[2] as Set<*>
                val selectedSubCategoriesArray = Array(selectedSubCategories.size) { SubCategory() }
                selectedSubCategories.forEachIndexed { i, subCategory ->
                    selectedSubCategoriesArray[i] = subCategory as SubCategory
                }

                SubCategoryScreenUIState(
                    it[0] as String,
                    it[1] as Boolean,
                    *selectedSubCategoriesArray
                )
            }
        )
    }
}

@Composable
fun rememberSubCategoryScreenUIState(mainCategoryName: String) =
    rememberSaveable(saver = SubCategoryScreenUIState.Saver) {
        SubCategoryScreenUIState(mainCategoryName)
    }


class EditSubCategoryNameDialogUIState(
    categoryName: String = "",
    error: Int? = null,
    showDialog: Boolean = false
) : BaseSubCategoryTextFieldDialogUIState(categoryName, error, showDialog) {
    fun showDialog(categoryName: String) {
        text = categoryName
        showDialog = true
    }

    companion object {
        val Saver: Saver<EditSubCategoryNameDialogUIState, *> = listSaver(
            save = {
                listOf(
                    it.text,
                    it.error,
                    it.showDialog
                )
            },
            restore = {
                EditSubCategoryNameDialogUIState(
                    it[0] as String, it[1] as? Int, it[2] as Boolean
                )
            }
        )
    }
}

@Composable
fun rememberEditSubCategoryNameDialogUIState() =
    rememberSaveable(saver = EditSubCategoryNameDialogUIState.Saver) {
        EditSubCategoryNameDialogUIState()
    }

fun <T> Set<T>.taskAndReturn(task: (MutableList<T>) -> Unit): Set<T> {
    val temp = toMutableList()
    task(temp)
    return temp.toSet()
}