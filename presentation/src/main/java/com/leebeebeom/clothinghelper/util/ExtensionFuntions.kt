package com.leebeebeom.clothinghelper.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.navigation.NavHostController
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet

inline fun <T> Collection<T>.taskAndReturnSet(crossinline task: (MutableSet<T>) -> Unit): ImmutableSet<T> {
    val temp = toMutableSet()
    task(temp)
    return temp.toImmutableSet()
}

fun NavHostController.navigateSingleTop(route: String) = navigate(route) {
    launchSingleTop = true
}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}