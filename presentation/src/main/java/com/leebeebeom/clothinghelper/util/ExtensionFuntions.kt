package com.leebeebeom.clothinghelper.util

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