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
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.SimpleToast
import com.leebeebeom.clothinghelper.theme.ClothingHelperTheme
import com.leebeebeom.clothinghelperdomain.model.EssentialMenu
import com.leebeebeom.clothinghelperdomain.model.EssentialMenus
import com.leebeebeom.clothinghelperdomain.model.MainCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainScreenRoot(
    onSettingIconClick: () -> Unit,
    onDrawerContentClick: (parentName: String) -> Unit,
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
                    onDrawerContentClick = {
                        onDrawerContentClick(it)
                        state.onDrawerClose()
                    },
                    onSettingIconClick = {
                        onSettingIconClick()
                        state.onDrawerClose()
                    },
                    essentialMenus = state.essentialMenus,
                    mainCategories = state.mainCategories,
                    getSubCategories = viewModelState::getSubCategories,
                    getIsSubCategoriesLoading = viewModelState::getIsSubCategoriesLoading
                )
            },
            drawerShape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
            drawerBackgroundColor = MaterialTheme.colors.primary,
            content = { content(it, viewModelState::getIsSubCategoriesLoading) })
        if (state.drawerState.isOpen) BackHandler(onBack = state::onDrawerClose)
    }
}

class MainScreenUIState(
    val scaffoldState: ScaffoldState,
    private val coroutineScope: CoroutineScope,
) {
    val drawerState: DrawerState = scaffoldState.drawerState

    fun onDrawerClose() {
        coroutineScope.launch {
            drawerState.close()
        }
    }

    val essentialMenus = listOf(
        EssentialMenu(R.string.main_screen, R.drawable.ic_home, EssentialMenus.MAIN_SCREEN),
        EssentialMenu(R.string.favorite, R.drawable.ic_star, EssentialMenus.FAVORITE),
        EssentialMenu(R.string.see_all, R.drawable.ic_list, EssentialMenus.SEE_ALL),
        EssentialMenu(R.string.trash, R.drawable.ic_delete, EssentialMenus.TRASH)
    )

    val mainCategories = getMainCategories()
}

@Composable
fun rememberMainScreenUIState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember {
    MainScreenUIState(scaffoldState, coroutineScope)
}

fun getMainCategories() = listOf(
    MainCategory(R.string.top, SubCategoryParent.TOP),
    MainCategory(R.string.bottom, SubCategoryParent.BOTTOM),
    MainCategory(R.string.outer, SubCategoryParent.OUTER),
    MainCategory(R.string.etc, SubCategoryParent.ETC)
)