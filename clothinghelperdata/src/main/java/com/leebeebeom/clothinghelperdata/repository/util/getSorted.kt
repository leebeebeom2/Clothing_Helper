package com.leebeebeom.clothinghelperdata.repository.util

import com.leebeebeom.clothinghelperdomain.model.Order
import com.leebeebeom.clothinghelperdomain.model.Sort
import com.leebeebeom.clothinghelperdomain.model.SortPreferences
import com.leebeebeom.clothinghelperdomain.model.container.BaseContainer

fun <T : BaseContainer> getSorted(
    subCategories: List<T>, sortPreferences: SortPreferences
): List<T> {
    val sort = sortPreferences.sort
    val order = sortPreferences.order

    return when {
        sort == Sort.NAME && order == Order.ASCENDING -> subCategories.sortedBy { it.name }
        sort == Sort.NAME && order == Order.DESCENDING -> subCategories.sortedByDescending { it.name }
        sort == Sort.CREATE && order == Order.ASCENDING -> subCategories.sortedBy { it.createDate }
        sort == Sort.CREATE && order == Order.DESCENDING -> subCategories.sortedByDescending { it.createDate }
        sort == Sort.EDIT && order == Order.ASCENDING -> subCategories.sortedBy { it.editDate }
        sort == Sort.EDIT && order == Order.DESCENDING -> subCategories.sortedByDescending { it.editDate }
        else -> subCategories
    }
}