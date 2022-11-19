package com.leebeebeom.clothinghelper.map

import com.leebeebeom.clothinghelperdomain.model.container.BaseContainer
import com.leebeebeom.clothinghelperdomain.model.container.SubCategory
import com.leebeebeom.clothinghelperdomain.model.container.SubCategoryParent

data class StableSubCategory(
    val parent: SubCategoryParent = SubCategoryParent.TOP,
    override val key: String = "",
    override val name: String = "",
    override val createDate: Long = 0,
    override val editDate: Long = 0
) : BaseContainer() {
}

fun SubCategory.toStable() =
    StableSubCategory(
        parent = parent,
        key = key,
        name = name,
        createDate = createDate,
        editDate = editDate
    )

fun StableSubCategory.toUnstable() =
    SubCategory(
        parent = parent,
        key = key,
        name = name,
        createDate = createDate,
        editDate = editDate
    )