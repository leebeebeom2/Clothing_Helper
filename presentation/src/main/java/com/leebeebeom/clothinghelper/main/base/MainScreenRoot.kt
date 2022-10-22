package com.leebeebeom.clothinghelper.main.base

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.base.SimpleIcon
import com.leebeebeom.clothinghelper.base.SimpleToast
import com.leebeebeom.clothinghelper.base.SimpleWidthSpacer
import com.leebeebeom.clothinghelper.main.subcategory.ExpandIcon
import com.leebeebeom.clothinghelper.theme.ClothingHelperTheme
import com.leebeebeom.clothinghelper.theme.Disabled
import com.leebeebeom.clothinghelperdomain.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainScreenRoot(
    onSettingIconClick: () -> Unit,
    onDrawerContentClick: (Int) -> Unit,
    viewModel: MainScreenRootViewModel = hiltViewModel(),
    content: @Composable (PaddingValues) -> Unit
) {
    val viewModelState = viewModel.viewModelState
    val state = rememberMainScreenUIState()

    viewModelState.toastText?.let {
        SimpleToast(text = it, viewModelState.toastShown)
    }

    ClothingHelperTheme {
        Scaffold(
            scaffoldState = state.scaffoldState,
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
                    getIsLoading = viewModelState::getIsLoading
                )
            },
            drawerShape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
            drawerBackgroundColor = MaterialTheme.colors.primary,
            bottomBar = {
                CHBottomAppBar(onDrawerIconClick = state::onDrawerIconClick)
            },
            content = content
        )
        if (state.drawerState.isOpen) BackHandler(onBack = state::onDrawerClose)
    }
}

@Composable
private fun CHBottomAppBar(onDrawerIconClick: () -> Unit) {
    BottomAppBar(contentPadding = PaddingValues(horizontal = 4.dp)) {
        IconButton(onClick = onDrawerIconClick) {
            SimpleIcon(drawable = R.drawable.ic_menu)
        }
    }
}

@Composable
private fun DrawerContents(
    user: User?,
    onDrawerContentClick: (Int) -> Unit,
    onSettingIconClick: () -> Unit,
    essentialMenus: List<EssentialMenu>,
    mainCategories: List<MainCategory>,
    getSubCategories: (SubCategoryParent) -> List<SubCategory>,
    getIsLoading: (SubCategoryParent) -> Boolean
) {
    Column {
        DrawerHeader(user = user, onSettingIconClick = onSettingIconClick)
        Surface(color = Color(0xFF121212)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 4.dp, bottom = 40.dp)
            ) {
                items(essentialMenus,
                    key = { it.id }) {
                    EssentialMenu(
                        essentialMenu = it, onDrawerContentClick = onDrawerContentClick
                    )
                }
                item {
                    SimpleHeightSpacer(dp = 4)
                    Divider(color = Disabled)
                    SimpleHeightSpacer(dp = 4)
                }

                items(mainCategories, key = { it.id }) {
                    MainCategory(
                        mainCategory = it,
                        subCategories = getSubCategories(it.type),
                        isLoading = getIsLoading(it.type),
                        onDrawerContentClick = onDrawerContentClick
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawerHeader(
    user: User?, onSettingIconClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        user?.let {
            Text(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f),
                style = MaterialTheme.typography.body1,
                text = "${user.name}(${user.email})"
            )
        }
        IconButton(onClick = onSettingIconClick) {
            SimpleIcon(drawable = R.drawable.ic_settings)
        }
    }
}

@Composable
private fun EssentialMenu(
    essentialMenu: EssentialMenu,
    onDrawerContentClick: (Int) -> Unit
) =
    DrawerContentRow(
        modifier = Modifier.heightIn(44.dp),
        onDrawerContentClick = { onDrawerContentClick(essentialMenu.id) }) {
        SimpleIcon(modifier = Modifier.size(22.dp), drawable = essentialMenu.drawable)
        SimpleWidthSpacer(dp = 12)
        DrawerContentText(
            modifier = Modifier.weight(1f),
            text = stringResource(id = essentialMenu.name),
            style = MaterialTheme.typography.body1.copy(letterSpacing = 0.75.sp)
        )
    }

@Composable
private fun DrawerContentText(modifier: Modifier, text: String, style: TextStyle) {
    Text(
        modifier = modifier,
        text = text,
        style = style,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun MainCategory(
    mainCategory: MainCategory,
    subCategories: List<SubCategory>,
    isLoading: Boolean,
    onDrawerContentClick: (Int) -> Unit,
) {
    var isExpand by rememberSaveable { mutableStateOf(false) }

    Column {
        DrawerContentRow(
            modifier = Modifier.heightIn(48.dp),
            onDrawerContentClick = { onDrawerContentClick(mainCategory.id) }) {
            DrawerContentText(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                text = stringResource(id = mainCategory.name),
                style = MaterialTheme.typography.subtitle1
            )
            if (isLoading) CircularProgressIndicator(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(16.dp),
                color = MaterialTheme.colors.surface.copy(ContentAlpha.medium),
                strokeWidth = 2.dp
            )
            else ExpandIcon(modifier = Modifier.size(22.dp), isExpanded = isExpand) {
                isExpand = !isExpand
            }
        }
        AnimatedVisibility(
            visible = isExpand,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Surface(color = MaterialTheme.colors.primary) {
                Column {
                    for (subCategory in subCategories) SubCategory(subCategory) {}
                }
            }
        }
    }

}

@Composable
private fun SubCategory(subCategory: SubCategory, onSubCategoryClick: (id: Long) -> Unit) {
    DrawerContentRow(
        modifier = Modifier.heightIn(40.dp),
        onDrawerContentClick = { onSubCategoryClick(subCategory.id) }) {
        DrawerContentText(
            modifier = Modifier
                .weight(1f)
                .padding(start = 20.dp),
            text = subCategory.name,
            style = MaterialTheme.typography.subtitle2
        )
    }
}

@Composable
private fun DrawerContentRow(
    modifier: Modifier, onDrawerContentClick: () -> Unit, content: @Composable RowScope.() -> Unit
) = Row(
    modifier = modifier
        .fillMaxWidth()
        .padding(start = 8.dp, end = 8.dp)
        .clip(RoundedCornerShape(12.dp))
        .clickable(onClick = onDrawerContentClick)
        .padding(start = 4.dp),
    verticalAlignment = Alignment.CenterVertically,
    content = content
)

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

    fun onDrawerIconClick() {
        coroutineScope.launch {
            if (drawerState.isOpen) drawerState.close()
            else drawerState.open()
        }
    }

    val essentialMenus = listOf(
        EssentialMenu(BaseMenuIds.MAIN_SCREEN, R.string.main_screen, R.drawable.ic_home),
        EssentialMenu(BaseMenuIds.FAVORITE, R.string.favorite, R.drawable.ic_star),
        EssentialMenu(BaseMenuIds.SEE_ALL, R.string.see_all, R.drawable.ic_list),
        EssentialMenu(BaseMenuIds.TRASH, R.string.trash, R.drawable.ic_delete)
    )

    val mainCategories = listOf(
        MainCategory(BaseMenuIds.TOP, R.string.top, SubCategoryParent.Top),
        MainCategory(BaseMenuIds.BOTTOM, R.string.bottom, SubCategoryParent.Bottom),
        MainCategory(BaseMenuIds.OUTER, R.string.outer, SubCategoryParent.OUTER),
        MainCategory(BaseMenuIds.ETC, R.string.etc, SubCategoryParent.ETC),
    )
}

@Composable
fun rememberMainScreenUIState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember {
    MainScreenUIState(scaffoldState, coroutineScope)
}