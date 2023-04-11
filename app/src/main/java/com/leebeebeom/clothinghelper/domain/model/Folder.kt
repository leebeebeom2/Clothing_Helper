package com.leebeebeom.clothinghelper.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Folder(
    override val key: String = "",
    val name: String = "",
    val parentKey: String = "",
    val createDate: Long = System.currentTimeMillis(),
    val editDate: Long = System.currentTimeMillis(),
) : BaseModel(), Parcelable {
    override fun addKey(key: String) = copy(key = key)
    fun changeEditDate() = copy(editDate = System.currentTimeMillis())
}