package com.leebeebeom.clothinghelper.domain.model.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.leebeebeom.clothinghelper.ui.main.drawer.MainCategoryType
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class SubCategory(
    @PrimaryKey override val key: String = "",
    override val parent: MainCategoryType = MainCategoryType.TOP,
    override val name: String = "",
    override val createDate: Long = 0,
    override val editDate: Long = 0,
    override val isSynced: Boolean = false,
) : BaseContainerModel(), Parcelable {
    override fun addKey(key: String) = copy(key = key)
}