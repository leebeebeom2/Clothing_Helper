package com.leebeebeom.clothinghelper.ui.main

import android.content.Context
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.OnClick
import com.leebeebeom.clothinghelper.ui.*
import com.leebeebeom.clothinghelper.ui.signin.SignInActivity
import com.leebeebeom.clothinghelper.ui.theme.Disabled
import com.leebeebeom.clothinghelper.ui.theme.Primary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainActivityRoot(
    onSettingIconClick: () -> Unit,
    viewModel: MainViewModel = viewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    drawerState: DrawerState = scaffoldState.drawerState,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onDrawerClose: () -> Unit = {
        coroutineScope.launch { drawerState.close() }
        Unit
    },
    onDrawerContentClick: OnClick,
    content: @Composable (PaddingValues) -> Unit
) {
    if (!viewModel.isLogin) StartSignInActivity(isLogin = viewModel.isLogin)

    ThemeRoot {
        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = {
                DrawerContents(
                    onSettingIconClick = {
                        onSettingIconClick()
                        onDrawerClose()
                    },
                    userNameAndEmail = viewModel.userNameAndEmail,
                    onDrawerContentClick = {
                        onDrawerContentClick()
                        onDrawerClose()
                    }
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
            onBackPress = onDrawerClose
        )
    }
}

@Composable
private fun StartSignInActivity(isLogin: Boolean, context: Context = LocalContext.current) {
    LaunchedEffect(isLogin) {
        delay(500)
        if (!isLogin) context.startActivity(Intent(context, SignInActivity::class.java))
    }
}

@Composable
fun DrawerBackHandler(isDrawerOpen: Boolean, onBackPress: () -> Unit) =
    BackHandler(enabled = isDrawerOpen, onBack = onBackPress)

@Composable
fun CHBottomAppBar(onDrawerToggle: () -> Unit) {
    BottomAppBar(
        contentColor = Color.White,
        contentPadding = PaddingValues(horizontal = 4.dp),
        backgroundColor = Color.Black
    ) {
        DrawerIcon(onDrawerToggle)
    }
}

@Composable
fun DrawerIcon(onDrawerIconClick: () -> Unit) = ClickableIcon(
    drawableId = R.drawable.ic_drawer,
    onClick = onDrawerIconClick,
    contentDescription = "drawer icon"
)

@Composable
fun DrawerContents(
    onSettingIconClick: () -> Unit,
    userNameAndEmail: String?,
    onDrawerContentClick: OnClick
) {
    Column {
        DrawerHeader(
            onSettingIconClick = onSettingIconClick, userNameAndEmail = userNameAndEmail
        )
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .background(Color(0xFF121212))
        ) {
            SimpleHeightSpacer(dp = 4)
            DrawerContent(
                drawableResId = R.drawable.ic_star,
                stringResId = R.string.favorite,
                onDrawerContentClick = onDrawerContentClick
            )
            DrawerContent(
                drawableResId = R.drawable.ic_list,
                stringResId = R.string.see_all,
                onDrawerContentClick = onDrawerContentClick
            )
            DrawerContent(
                drawableResId = R.drawable.ic_trash,
                stringResId = R.string.trash_can,
                onDrawerContentClick = onDrawerContentClick
            )
            SimpleHeightSpacer(dp = 4)
            Divider(color = Disabled)
            SimpleHeightSpacer(dp = 4)
            DrawerContent(
                drawableResId = R.drawable.ic_list,
                stringResId = R.string.top,
                onDrawerContentClick = onDrawerContentClick
            )
            DrawerContent(
                drawableResId = R.drawable.ic_list,
                stringResId = R.string.bottom,
                onDrawerContentClick = onDrawerContentClick
            )
            DrawerContent(
                drawableResId = R.drawable.ic_list,
                stringResId = R.string.outer,
                onDrawerContentClick = onDrawerContentClick
            )
            for (i in 0..20) DrawerContent(
                drawableResId = R.drawable.ic_list,
                stringResId = R.string.etc,
                onDrawerContentClick = onDrawerContentClick
            )
            SimpleHeightSpacer(dp = 40)
        }
    }
}

@Composable
private fun DrawerHeader(
    onSettingIconClick: () -> Unit, userNameAndEmail: String?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        UserNameText(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 12.dp),
            userNameAndEmail = userNameAndEmail
        )
        SettingIcon(
            modifier = Modifier.align(Alignment.CenterEnd), onSettingIconClick = onSettingIconClick
        )
    }
}

@Composable
fun UserNameText(modifier: Modifier, userNameAndEmail: String?) {
    userNameAndEmail?.let {
        Text(
            modifier = modifier, style = MaterialTheme.typography.body1, text = it
        )
    }
}

@Composable
private fun SettingIcon(modifier: Modifier, onSettingIconClick: () -> Unit) = ClickableIcon(
    modifier = modifier,
    drawableId = R.drawable.ic_settings,
    contentDescription = "setting icon",
    onClick = onSettingIconClick
)

@Composable
fun DrawerContent(drawableResId: Int, stringResId: Int, onDrawerContentClick: OnClick) {
    DrawerContentRow(onDrawerContentClick) {
        SimpleIcon(
            modifier = Modifier.size(20.dp), drawableId = drawableResId
        )
        SimpleWidthSpacer(dp = 12)
        Text(
            text = stringResource(id = stringResId), style = MaterialTheme.typography.body1.copy(
                letterSpacing = 0.75.sp, color = Color.White
            )
        )
    }
}

@Composable
fun DrawerContentRow(onDrawerContentClick: OnClick, content: @Composable RowScope.() -> Unit) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp)
        .clip(RoundedCornerShape(12.dp))
        .clickable(onClick = onDrawerContentClick)
        .padding(start = 8.dp)
        .height(40.dp),
    verticalAlignment = Alignment.CenterVertically,
    content = content
)