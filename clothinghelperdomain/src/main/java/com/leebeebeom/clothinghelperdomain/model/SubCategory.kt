package com.leebeebeom.clothinghelperdomain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class SubCategoryParent {
    TOP, BOTTOM, OUTER, ETC
}

@Parcelize
data class SubCategory(
    val parent: SubCategoryParent = SubCategoryParent.TOP,
    val key: String = "",
    val name: String = "",
    val createDate: Long = 0
) : Parcelable
