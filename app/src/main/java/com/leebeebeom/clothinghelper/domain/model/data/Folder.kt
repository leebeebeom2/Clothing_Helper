package com.leebeebeom.clothinghelper.domain.model.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.leebeebeom.clothinghelper.ui.main.drawer.MainCategoryType
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Folder(
    @PrimaryKey override val key: String = "",
    override val parentKey: String = "",
    override val subCategoryKey: String = "",
    override val createDate: Long = 0,
    override val name: String = "",
    override val editDate: Long = 0,
    override val parent: MainCategoryType = MainCategoryType.TOP,
    override val isSynced: Boolean = false,
) : BaseFolderModel(), Parcelable {
    override fun addKey(key: String) = copy(key = key)
    override fun synced() = copy(isSynced = true)
}