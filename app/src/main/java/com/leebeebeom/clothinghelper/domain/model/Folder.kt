package com.leebeebeom.clothinghelper.domain.model

import android.os.Parcelable
import com.leebeebeom.clothinghelper.ui.main.drawer.MainCategoryType
import kotlinx.parcelize.Parcelize

@Parcelize
data class Folder(
    override val key: String = "",
    override val name: String = "",
    override val parentKey: String = "",
    override val subCategoryKey: String = "",
    override val mainCategoryType: MainCategoryType = MainCategoryType.TOP,
    override val createDate: Long = System.currentTimeMillis(),
    override val editDate: Long = System.currentTimeMillis(),
) : BaseFolderModel(), Parcelable {
    override fun addKey(key: String) = copy(key = key)

    override fun changeEditDate() = copy(editDate = System.currentTimeMillis())
}