package com.leebeebeom.clothinghelper.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.leebeebeom.clothinghelper.MainActivityNavigationRoute
import com.leebeebeom.clothinghelper.ui.navigate
import com.leebeebeom.clothinghelper.R

@Composable
fun HomeScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        HomeScreenDivider()
        Column {
            val weightModifier = Modifier
                .weight(1f)
                .fillMaxHeight()

            HomeScreenRow(weightModifier) {
                HomeScreenTextBox(modifier = weightModifier, textId = R.string.top) {
                    navigate(navController, MainActivityNavigationRoute.SUB_TYPE)
                }
                HomeScreenTextBox(modifier = weightModifier, textId = R.string.bottom) {
                }
            }
            HomeScreenRow(weightModifier) {
                HomeScreenTextBox(modifier = weightModifier, textId = R.string.outer) {

                }
                HomeScreenTextBox(modifier = weightModifier, textId = R.string.etc) {

                }
            }
        }
    }
}

@Composable
fun HomeScreenRow(modifier: Modifier, content: @Composable RowScope.() -> Unit) =
    Row(modifier = modifier.fillMaxHeight(), content = content)

@Composable
fun HomeScreenTextBox(modifier: Modifier, textId: Int, onClick: () -> Unit) {
    Box(modifier = modifier.clickable { onClick() }) {
        Text(
            text = stringResource(id = textId),
            style = MaterialTheme.typography.h4,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun HomeScreenDivider() {
    Divider(
        modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
    )
    Divider()
}