package com.leebeebeom.clothinghelper.util

fun <T> Collection<T>.taskAndReturn(task: (MutableList<T>) -> Unit): Set<T> {
    val temp = toMutableList()
    task(temp)
    return temp.toSet()
}