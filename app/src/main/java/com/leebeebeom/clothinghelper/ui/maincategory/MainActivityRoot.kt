package com.leebeebeom.clothinghelper.ui.maincategory

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.*
import com.leebeebeom.clothinghelper.ui.signin.FirebaseUseCase
import com.leebeebeom.clothinghelper.ui.signin.SignInActivity
import com.leebeebeom.clothinghelper.ui.theme.Disabled
import com.leebeebeom.clothinghelper.ui.theme.Primary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainActivityRoot(
    onNavigationSetting: () -> Unit,
    viewModel: MainViewModel = viewModel(),
    content: @Composable (PaddingValues) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val drawerState = scaffoldState.drawerState
    val coroutineScope = rememberCoroutineScope()
    val onDrawerClose = {
        coroutineScope.launch { drawerState.close() }
        Unit
    }

    if (!viewModel.isLogin) StartSignInActivity(viewModel.isLogin)

    ThemeRoot {
        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = {
                DrawerContents(
                    onNavigationSetting = onNavigationSetting,
                    onDrawerClose = onDrawerClose
                )
            },
            drawerShape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
            drawerBackgroundColor = Primary,
            bottomBar = {
                CHBottomAppBar {
                    coroutineScope.launch {
                        if (drawerState.isOpen) drawerState.close()
                        else drawerState.open()
                    }
                }
            },
            content = content
        )
        DrawerBackHandler(
            isDrawerOpen = drawerState.isOpen,
            onDrawerClose = onDrawerClose
        )
    }
}

@Composable
private fun StartSignInActivity(isLogin: Boolean) {
    val context = LocalContext.current

    LaunchedEffect(isLogin) {
        delay(500)
        if (!isLogin) context.startActivity(Intent(context, SignInActivity::class.java))
    }
}

@Composable
fun DrawerBackHandler(isDrawerOpen: Boolean, onDrawerClose: () -> Unit) {
    BackHandler(enabled = isDrawerOpen, onBack = onDrawerClose)
}

@Composable
fun CHBottomAppBar(onDrawerToggle: () -> Unit) {
    BottomAppBar(
        contentColor = Color.White, contentPadding = PaddingValues(horizontal = 4.dp),
        backgroundColor = Color.Black
    ) {
        DrawerIcon(onDrawerToggle)
    }
}

@Composable
fun DrawerIcon(onDrawerToggle: () -> Unit) =
    ClickableIcon(
        drawableId = R.drawable.ic_drawer,
        onClick = onDrawerToggle,
        contentDescription = "drawer icon"
    )

@Composable
fun DrawerContents(onNavigationSetting: () -> Unit, onDrawerClose: () -> Unit) {
    Column {
        DrawerHeader(
            onNavigationSetting = onNavigationSetting,
            onDrawerClose = onDrawerClose
        )
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .background(Color(0xFF121212))
        ) {
            SimpleHeightSpacer(dp = 4)
            DrawerContent(drawableResId = R.drawable.ic_star, stringResId = R.string.favorite)
            DrawerContent(drawableResId = R.drawable.ic_list, stringResId = R.string.see_all)
            DrawerContent(drawableResId = R.drawable.ic_trash, stringResId = R.string.trash_can)
            SimpleHeightSpacer(dp = 4)
            Divider(color = Disabled)
            SimpleHeightSpacer(dp = 4)
            DrawerContent(drawableResId = R.drawable.ic_list, stringResId = R.string.top)
            DrawerContent(drawableResId = R.drawable.ic_list, stringResId = R.string.bottom)
            DrawerContent(drawableResId = R.drawable.ic_list, stringResId = R.string.outer)
            for (i in 0..20)
                DrawerContent(drawableResId = R.drawable.ic_list, stringResId = R.string.etc)
            SimpleHeightSpacer(dp = 40)
        }
    }
}

@Composable
private fun DrawerHeader(onNavigationSetting: () -> Unit, onDrawerClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        UserNameText(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 12.dp)
        )
        SettingIcon(onNavigationSetting, onDrawerClose)
    }
}

@Composable
fun UserNameText(modifier: Modifier) {
    val nameAndEmail = "${FirebaseUseCase.userName}(${FirebaseUseCase.userEmail})"
    Text(
        text = nameAndEmail,
        modifier = modifier,
        style = MaterialTheme.typography.body1,
    )
}

@Composable
private fun SettingIcon(onNavigationSetting: () -> Unit, onDrawerClose: () -> Unit) {
    ClickableIcon(drawableId = R.drawable.ic_settings, contentDescription = "setting icon") {
        onDrawerClose()
        onNavigationSetting()
    }
}

@Composable
fun DrawerContent(drawableResId: Int, stringResId: Int) {
    DrawerContentRow {
        Icon(
            painter = painterResource(id = drawableResId),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        SimpleWidthSpacer(dp = 12)
        Text(
            text = stringResource(id = stringResId),
            style = MaterialTheme.typography.body1.copy(
                letterSpacing = 0.75.sp,
                color = Color.White

            )
        )
    }
}

@Composable
fun DrawerContentRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { }
            .padding(start = 8.dp)
            .height(40.dp),
        content = content,
        verticalAlignment = Alignment.CenterVertically,
    )
}