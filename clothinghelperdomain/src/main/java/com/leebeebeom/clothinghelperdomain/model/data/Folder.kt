package com.leebeebeom.clothinghelperdomain.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Folder(
    override val parentKey: String = "",
    override val subCategoryKey: String = "",
    override val createDate: Long = 0,
    override val name: String = "",
    override val key: String = "",
    override val editDate: Long = 0,
    override val parent: SubCategoryParent = SubCategoryParent.TOP
) : BaseFolderModel(), Parcelable {
    override fun addKey(key: String) = copy(key = key)
}