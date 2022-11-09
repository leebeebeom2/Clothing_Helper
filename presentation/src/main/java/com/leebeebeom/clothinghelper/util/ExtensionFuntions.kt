package com.leebeebeom.clothinghelper.util

import androidx.navigation.NavHostController

inline fun <T> Collection<T>.taskAndReturn(crossinline task: (MutableList<T>) -> Unit): Set<T> {
    val temp = toMutableList()
    task(temp)
    return temp.toSet()
}

fun NavHostController.navigateSingleTop(route: String) = navigate(route) {
    launchSingleTop = true
}