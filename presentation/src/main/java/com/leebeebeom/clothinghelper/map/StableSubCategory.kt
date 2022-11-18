package com.leebeebeom.clothinghelper.map

import com.leebeebeom.clothinghelperdomain.model.container.SubCategory
import com.leebeebeom.clothinghelperdomain.model.container.SubCategoryParent

data class StableSubCategory(
    val parent: SubCategoryParent = SubCategoryParent.TOP,
    val key: String = "",
    val name: String = "",
    val createDate: Long = 0
)

fun SubCategory.toStable() =
    StableSubCategory(parent = parent, key = key, name = name, createDate = createDate)

fun StableSubCategory.toUnstable() =
    SubCategory(parent = parent, key = key, name = name, createDate = createDate)