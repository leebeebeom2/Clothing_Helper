package com.leebeebeom.clothinghelper.ui.main

import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.domain.OnClick
import com.leebeebeom.clothinghelper.ui.CenterCircularProgressIndicator
import com.leebeebeom.clothinghelper.ui.SimpleHeightSpacer
import com.leebeebeom.clothinghelper.ui.SimpleIcon
import com.leebeebeom.clothinghelper.ui.SimpleWidthSpacer
import com.leebeebeom.clothinghelper.ui.signin.SignInActivity
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import com.leebeebeom.clothinghelper.ui.theme.Disabled
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
    val mainState = viewModel.mainState

    StartSignInActivity(
        isLogin = mainState.isLogin,
        loadingOn = viewModel.loadingOn,
        loadingOff = viewModel.loadingOff
    )

    ClothingHelperTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = {
                DrawerContents(
                    onSettingIconClick = {
                        onSettingIconClick()
                        onDrawerClose()
                    },
                    userName = mainState.userName,
                    userEmail = mainState.userEmail,
                    onDrawerContentClick = {
                        onDrawerContentClick()
                        onDrawerClose()
                    }
                )
            },
            drawerShape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
            drawerBackgroundColor = MaterialTheme.colors.primary,
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
        if (mainState.isLoading) CenterCircularProgressIndicator()
        if (drawerState.isOpen) BackHandler(onBack = onDrawerClose)
    }
}

@Composable
private fun StartSignInActivity(
    isLogin: Boolean,
    loadingOn: () -> Unit,
    loadingOff: () -> Unit,
    context: Context = LocalContext.current
) {
    LaunchedEffect(isLogin) {
        loadingOn()
        delay(300)
        if (!isLogin) context.startActivity(Intent(context, SignInActivity::class.java))
        loadingOff()
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
    onSettingIconClick: () -> Unit,
    userName: String,
    userEmail: String,
    onDrawerContentClick: OnClick
) {
    Column {
        DrawerHeader(
            onSettingIconClick = onSettingIconClick,
            userName = userName,
            userEmail = userEmail
        )
        Surface(color = Color(0xFF121212)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 4.dp, bottom = 40.dp)
            ) {
                item {
                    DrawerContent(
                        drawable = R.drawable.ic_home,
                        text = R.string.main_screen,
                        onDrawerContentClick = onDrawerContentClick
                    )
                    DrawerContent(
                        drawable = R.drawable.ic_star,
                        text = R.string.favorite,
                        onDrawerContentClick = onDrawerContentClick
                    )
                    DrawerContent(
                        drawable = R.drawable.ic_list,
                        text = R.string.see_all,
                        onDrawerContentClick = onDrawerContentClick
                    )
                    DrawerContent(
                        drawable = R.drawable.ic_delete,
                        text = R.string.trash_can,
                        onDrawerContentClick = onDrawerContentClick
                    )
                    SimpleHeightSpacer(dp = 4)
                    Divider(color = Disabled)
                    SimpleHeightSpacer(dp = 4)
                    DrawerContent(
                        drawable = R.drawable.ic_list,
                        text = R.string.top,
                        onDrawerContentClick = onDrawerContentClick
                    )
                    DrawerContent(
                        drawable = R.drawable.ic_list,
                        text = R.string.bottom,
                        onDrawerContentClick = onDrawerContentClick
                    )
                    DrawerContent(
                        drawable = R.drawable.ic_list,
                        text = R.string.outer,
                        onDrawerContentClick = onDrawerContentClick
                    )
                    DrawerContent(
                        drawable = R.drawable.ic_list,
                        text = R.string.etc,
                        onDrawerContentClick = onDrawerContentClick
                    )
                }
                items(20) {
                    DrawerContent(
                        drawable = R.drawable.ic_list,
                        text = R.string.etc,
                        onDrawerContentClick = onDrawerContentClick
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawerHeader(
    onSettingIconClick: () -> Unit,
    userName: String,
    userEmail: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f),
            style = MaterialTheme.typography.body1,
            text = "$userName($userEmail)"
        )
        IconButton(onClick = onSettingIconClick) {
            SimpleIcon(drawable = R.drawable.ic_settings)
        }
    }
}

@Composable
private fun DrawerContent(
    @DrawableRes drawable: Int,
    @StringRes text: Int,
    onDrawerContentClick: OnClick
) {
    DrawerContentRow(onDrawerContentClick) {
        SimpleIcon(modifier = Modifier.size(22.dp), drawable = drawable)
        SimpleWidthSpacer(dp = 12)
        Text(
            text = stringResource(id = text),
            style = MaterialTheme.typography.body1.copy(letterSpacing = 0.75.sp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun DrawerContentRow(
    onDrawerContentClick: OnClick,
    content: @Composable RowScope.() -> Unit
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp)
        .clip(RoundedCornerShape(12.dp))
        .clickable(onClick = onDrawerContentClick)
        .padding(start = 8.dp)
        .heightIn(40.dp),
    verticalAlignment = Alignment.CenterVertically,
    content = content
)