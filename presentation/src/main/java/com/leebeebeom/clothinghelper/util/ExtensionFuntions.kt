package com.leebeebeom.clothinghelper.util

import androidx.navigation.NavHostController

inline fun <T> Collection<T>.taskAndReturnSet(crossinline task: (MutableSet<T>) -> Unit): Set<T> {
    val temp = toMutableSet()
    task(temp)
    return temp
}

fun NavHostController.navigateSingleTop(route: String) = navigate(route) {
    launchSingleTop = true
}