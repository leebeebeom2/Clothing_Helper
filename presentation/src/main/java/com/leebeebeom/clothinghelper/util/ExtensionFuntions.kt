package com.leebeebeom.clothinghelper.util

inline fun <T> Collection<T>.taskAndReturn(crossinline task: (MutableList<T>) -> Unit): Set<T> {
    val temp = toMutableList()
    task(temp)
    return temp.toSet()
}