package com.leebeebeom.clothinghelper.main.base

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.base.SimpleToast
import com.leebeebeom.clothinghelper.theme.ClothingHelperTheme
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainScreenRoot(
    onSettingIconClick: () -> Unit,
    onEssentialMenuClick: (name: String) -> Unit,
    onMainCategoryClick: (name: String) -> Unit,
    onSubCategoryClick: (key: String) -> Unit,
    viewModel: MainScreenRootViewModel = hiltViewModel(),
    content: @Composable (
        PaddingValues, getIsSubCategoriesLoading: (SubCategoryParent) -> Boolean
    ) -> Unit,
) {
    val viewModelState = viewModel.viewModelState
    val state = rememberMainScreenUIState()

    viewModelState.toastText?.let {
        SimpleToast(text = it, viewModelState.toastShown)
    }

    ClothingHelperTheme {
        Scaffold(scaffoldState = state.scaffoldState,
            drawerContent = {
                DrawerContents(
                    user = viewModelState.user,
                    onEssentialMenuClick = {
                        onEssentialMenuClick(it)
                        state.onDrawerClose()
                    },
                    onMainCategoryClick = {
                        onMainCategoryClick(it)
                        state.onDrawerClose()
                    },
                    onSubCategoryClick = {
                        onSubCategoryClick(it)
                        state.onDrawerClose()
                    },
                    onSettingIconClick = {
                        onSettingIconClick()
                        state.onDrawerClose()
                    },
                    getSubCategories = viewModelState.getSubCategories,
                    getIsSubCategoriesLoading = viewModelState.getIsSubCategoriesLoading
                )
            },
            drawerShape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
            drawerBackgroundColor = MaterialTheme.colors.primary,
            content = { content(it, viewModelState.getIsSubCategoriesLoading) })

        BackHandler(enabled = state.drawerState.isOpen, onBack = state::onDrawerClose)
    }
}

class MainScreenUIState(
    val scaffoldState: ScaffoldState,
    private val coroutineScope: CoroutineScope,
) {
    val drawerState: DrawerState = scaffoldState.drawerState

    fun onDrawerClose() {
        coroutineScope.launch { drawerState.close() }
    }
}

@Composable
fun rememberMainScreenUIState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember {
    MainScreenUIState(scaffoldState, coroutineScope)
}