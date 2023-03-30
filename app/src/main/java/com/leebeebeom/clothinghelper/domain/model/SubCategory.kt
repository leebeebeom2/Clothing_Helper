package com.leebeebeom.clothinghelper.domain.model

import android.os.Parcelable
import com.leebeebeom.clothinghelper.ui.drawer.contents.MainCategoryType
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubCategory(
    override val key: String = "",
    override val name: String = "",
    override val mainCategoryType: MainCategoryType = MainCategoryType.TOP,
    override val createDate: Long = System.currentTimeMillis(),
    override val editDate: Long = System.currentTimeMillis(),
) : BaseContainerModel(), Parcelable {
    override fun addKey(key: String) = copy(key = key)
    override fun changeEditDate() = copy(editDate = System.currentTimeMillis())
}