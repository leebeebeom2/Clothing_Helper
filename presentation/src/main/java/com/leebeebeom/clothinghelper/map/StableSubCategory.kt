package com.leebeebeom.clothinghelper.map

import com.leebeebeom.clothinghelperdomain.model.container.BaseModel
import com.leebeebeom.clothinghelperdomain.model.container.SubCategory
import com.leebeebeom.clothinghelperdomain.model.container.SubCategoryParent

data class StableSubCategory(
    override val parent: SubCategoryParent = SubCategoryParent.TOP,
    override val key: String = "",
    override val name: String = "",
    override val createDate: Long = 0,
    override val editDate: Long = 0
) : BaseModel()

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