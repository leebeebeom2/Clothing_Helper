package com.leebeebeom.clothinghelper.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.leebeebeom.clothinghelper.ui.main.drawer.MainCategoryType
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubCategory(
    override val key: String = "",
    override val name: String = "",
    val parent: MainCategoryType = MainCategoryType.TOP,
) : BaseContainerModel(), Parcelable {
    override fun addKey(key: String) = copy(key = key)
}

@Entity
data class DatabaseSubCategory(
    @PrimaryKey override val key: String = "",
    override val name: String = "",
    val parent: MainCategoryType = MainCategoryType.TOP,
    override val createDate: Long = 0,
    override val editDate: Long = 0,
) : BaseDatabaseContainerModel() {
    override fun addKey(key: String) = copy(key = key)
}