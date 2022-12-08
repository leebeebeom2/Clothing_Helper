package com.leebeebeom.clothinghelper.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController

fun NavHostController.navigateSingleTop(route: String) = navigate(route) {
    launchSingleTop = true
}

fun NavBackStackEntry?.getCurrentRoute() = this?.destination?.route
fun NavBackStackEntry?.getStringArg(key: String) = this?.arguments?.getString(key)

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}