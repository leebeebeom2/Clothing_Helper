package com.leebeebeom.clothinghelper.util

import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelperdomain.model.container.SubCategoryParent

fun getHeaderStringRes(parent: SubCategoryParent): Int {
    return when (parent) {
        SubCategoryParent.TOP -> R.string.top
        SubCategoryParent.BOTTOM -> R.string.bottom
        SubCategoryParent.OUTER -> R.string.outer
        SubCategoryParent.ETC -> R.string.etc
    }
}