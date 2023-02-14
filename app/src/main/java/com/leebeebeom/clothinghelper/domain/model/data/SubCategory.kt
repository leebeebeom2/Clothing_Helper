package com.leebeebeom.clothinghelper.domain.model.data

import android.os.Parcelable
import com.leebeebeom.clothinghelper.ui.main.drawer.MainCategoryType
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubCategory(
    override val parent: MainCategoryType = MainCategoryType.TOP,
    override val key: String = "",
    override val name: String = "",
    override val createDate: Long = 0,
    override val editDate: Long = 0
) : BaseModel(), Parcelable {
    override fun addKey(key: String) = copy(key = key)
}