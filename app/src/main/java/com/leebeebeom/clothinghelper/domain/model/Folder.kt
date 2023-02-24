package com.leebeebeom.clothinghelper.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class Folder(
    override val key: String = "",
    override val name: String = "",
    override val parentKey: String = "",
    override val subCategoryKey: String = "",
) : BaseFolderModel(), Parcelable

@Entity
data class RoomFolder(
    @PrimaryKey override val key: String = "",
    override val name: String = "",
    override val parentKey: String = "",
    override val subCategoryKey: String = "",
    override val createDate: Long = 0,
    override val editDate: Long = 0,
) : BaseRoomFolderModel() {
    override fun addKey(key: String) = copy(key = key)
}

data class FirebaseFolder(
    override val key: String = "",
    override val name: String = "",
    override val parentKey: String = "",
    override val subCategoryKey: String = "",
    override val createDate: Long = 0,
    override val editDate: Long = 0,
) : BaseFirebaseFolderModel()