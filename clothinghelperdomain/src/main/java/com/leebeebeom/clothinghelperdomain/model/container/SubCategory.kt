package com.leebeebeom.clothinghelperdomain.model.container

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubCategory(
    override val parent: SubCategoryParent = SubCategoryParent.TOP,
    override val key: String = "",
    override val name: String = "",
    override val createDate: Long = 0,
    override val editDate: Long = 0
) : BaseModel(), Parcelable

enum class SubCategoryParent {
    TOP, BOTTOM, OUTER, ETC
}