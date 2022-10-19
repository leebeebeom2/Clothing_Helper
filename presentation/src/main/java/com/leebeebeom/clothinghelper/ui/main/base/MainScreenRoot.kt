package com.leebeebeom.clothinghelper.ui.main.base

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.data.model.BaseMenu
import com.leebeebeom.clothinghelper.data.model.EssentialMenu
import com.leebeebeom.clothinghelper.ui.base.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.ui.base.SimpleIcon
import com.leebeebeom.clothinghelper.ui.base.SimpleWidthSpacer
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import com.leebeebeom.clothinghelper.ui.theme.Disabled
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

    ClothingHelperTheme {
        Scaffold(
            scaffoldState = state.scaffoldState,
            drawerContent = {
                DrawerContents(onSettingIconClick = {
                    onSettingIconClick()
                    state.onDrawerClose()
                },
                    name = viewModelState.name,
                    email = viewModelState.email,
                    onDrawerContentClick = {
                        onDrawerContentClick(it)
                        state.onDrawerClose()
                    })
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
    onSettingIconClick: () -> Unit, name: String, email: String, onDrawerContentClick: (Int) -> Unit
) {
    Column {
        DrawerHeader(name = name, email = email, onSettingIconClick = onSettingIconClick)
        Surface(color = Color(0xFF121212)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 4.dp, bottom = 40.dp)
            ) {
                items(BaseMenu.essentialMenus, key = { it.id }) {
                    DrawerContent(
                        essentialMenu = it, onDrawerContentClick = onDrawerContentClick
                    )
                }
                item {
                    SimpleHeightSpacer(dp = 4)
                    Divider(color = Disabled)
                    SimpleHeightSpacer(dp = 4)
                }

                items(BaseMenu.mainCategories, key = { it.id }) {
                    DrawerContent(
                        essentialMenu = it, onDrawerContentClick = onDrawerContentClick
                    )
                }
                items(20) {
                    DrawerContent(
                        essentialMenu = EssentialMenu(
                            1, R.string.error_same_category_name, R.drawable.ic_list
                        ), onDrawerContentClick = onDrawerContentClick
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawerHeader(
    name: String, email: String, onSettingIconClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f),
            style = MaterialTheme.typography.body1,
            text = "$name($email)"
        )
        IconButton(onClick = onSettingIconClick) {
            SimpleIcon(drawable = R.drawable.ic_settings)
        }
    }
}

@Composable
private fun DrawerContent(
    essentialMenu: EssentialMenu, onDrawerContentClick: (Int) -> Unit
) {
    DrawerContentRow(onDrawerContentClick = { onDrawerContentClick(essentialMenu.id) }) {
        SimpleIcon(modifier = Modifier.size(22.dp), drawable = essentialMenu.drawable)
        SimpleWidthSpacer(dp = 12)
        Text(
            text = stringResource(id = essentialMenu.name),
            style = MaterialTheme.typography.body1.copy(letterSpacing = 0.75.sp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun DrawerContentRow(
    onDrawerContentClick: () -> Unit, content: @Composable RowScope.() -> Unit
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp)
        .clip(RoundedCornerShape(12.dp))
        .clickable(onClick = onDrawerContentClick)
        .padding(start = 8.dp)
        .heightIn(40.dp),
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
}

@Composable
fun rememberMainScreenUIState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember {
    MainScreenUIState(scaffoldState, coroutineScope)
}