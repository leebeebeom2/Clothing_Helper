package com.leebeebeom.clothinghelperdomain.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubCategory(
    override val parent: SubCategoryParent = SubCategoryParent.TOP,
    override val key: String = "",
    override val name: String = "",
    override val createDate: Long = 0,
    override val editDate: Long = 0
) : BaseModel(), Parcelable {
    override fun addKey(key: String) = copy(key = key)
}

enum class SubCategoryParent {
    TOP, BOTTOM, OUTER, ETC
}