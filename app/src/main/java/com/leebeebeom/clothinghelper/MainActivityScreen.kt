package com.leebeebeom.clothinghelper

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.ui.*
import com.leebeebeom.clothinghelper.ui.theme.Disabled
import com.leebeebeom.clothinghelper.ui.theme.Primary
import kotlinx.coroutines.launch

@Composable
fun MainActivityScreen() {
    val scaffoldState = rememberScaffoldState()

    ThemeRoot {
        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = { DrawerContents() },
            drawerShape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
            bottomBar = { CHBottomAppBar(scaffoldState) },
            drawerBackgroundColor = Primary,
        ) {
            MainActivityContent(paddingValues = it)
        }
    }
    DrawerBackHandler(scaffoldState = scaffoldState)
}

@Composable
fun MainActivityContent(paddingValues: PaddingValues) {
    Box(modifier = Modifier.padding(paddingValues), contentAlignment = Alignment.Center) {
        MainActivityDivider()
        Column {
            val weightModifier = Modifier
                .weight(1f)
                .fillMaxHeight()

            MainActivityContentRow(weightModifier) {
                MainActivityTextBox(modifier = weightModifier, textId = R.string.top)
                MainActivityTextBox(modifier = weightModifier, textId = R.string.bottom)
            }
            MainActivityContentRow(weightModifier) {
                MainActivityTextBox(modifier = weightModifier, textId = R.string.outer)
                MainActivityTextBox(modifier = weightModifier, textId = R.string.etc)
            }
        }
    }
}

@Composable
private fun MainActivityContentRow(modifier: Modifier, content: @Composable RowScope.() -> Unit) =
    Row(modifier = modifier.fillMaxHeight(), content = content)

@Composable
private fun MainActivityTextBox(modifier: Modifier, textId: Int) {
    Box(modifier = modifier.clickable {  }) {
        Text(
            text = stringResource(id = textId),
            style = MaterialTheme.typography.h4,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun MainActivityDivider() {
    Divider(
        modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
    )
    Divider()
}

@Composable
fun DrawerBackHandler(scaffoldState: ScaffoldState) {
    val coroutineScope = rememberCoroutineScope()

    BackHandler(enabled = scaffoldState.drawerState.isOpen) {
        coroutineScope.launch { scaffoldState.drawerState.close() }
    }
}

@Composable
fun CHBottomAppBar(scaffoldState: ScaffoldState) {
    BottomAppBar(
        contentColor = Color.White, contentPadding = PaddingValues(horizontal = 20.dp),
        backgroundColor = Color.Black
    ) {
        DrawerIcon(scaffoldState)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SimpleIcon(drawableId = R.drawable.ic_settings)
        }
    }
}

@Composable
fun DrawerIcon(scaffoldState: ScaffoldState) {
    val coroutineScope = rememberCoroutineScope()

    ClickableIcon(drawableId = R.drawable.ic_drawer) {
        coroutineScope.launch {
            scaffoldState.drawerState.apply {
                if (isClosed) open() else close()
            }
        }
    }
}

@Composable
fun DrawerContents() {
        SimpleHeightSpacer(dp = 28)
        DrawerContent(drawableResId = R.drawable.ic_star, stringResId = R.string.favorite)
        DrawerContent(drawableResId = R.drawable.ic_list, stringResId = R.string.see_all)
        DrawerContent(drawableResId = R.drawable.ic_trash, stringResId = R.string.trash_can)
        SimpleHeightSpacer(dp = 12)
        Divider(color = Disabled)
        SimpleHeightSpacer(dp = 12)
        DrawerContent(drawableResId = R.drawable.ic_list, stringResId = R.string.top)
        DrawerContent(drawableResId = R.drawable.ic_list, stringResId = R.string.bottom)
        DrawerContent(drawableResId = R.drawable.ic_list, stringResId = R.string.outer)
        DrawerContent(drawableResId = R.drawable.ic_list, stringResId = R.string.etc)
        Button(onClick = {FirebaseAuth.getInstance().signOut()}) {
            Text(text = "로그아웃")
        }
}

@Composable
fun DrawerContent(drawableResId: Int, stringResId: Int) {
    DrawerContentRow {
        SimpleWidthSpacer(dp = 24)
        Icon(
            painter = painterResource(id = drawableResId),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        SimpleWidthSpacer(dp = 12)
        ClickableText(
            text = AnnotatedString(text = stringResource(id = stringResId)),
            style = MaterialTheme.typography.body1.copy(
                letterSpacing = 0.75.sp,
                color = Color.White
            ),
            onClick = {}
        )
    }
}

@Composable
fun DrawerContentRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clickable { },
        content = content,
        verticalAlignment = Alignment.CenterVertically,
    )
}

@Preview
@Composable
fun MainActivityPreview() {
    MainActivityScreen()
}