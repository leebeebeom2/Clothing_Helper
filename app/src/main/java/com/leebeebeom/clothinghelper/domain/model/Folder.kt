package com.leebeebeom.clothinghelper.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.leebeebeom.clothinghelper.ui.main.drawer.MainCategoryType
import kotlinx.parcelize.Parcelize

@Parcelize
data class Folder(
    override val key: String = "",
    override val name: String = "",
    override val parentKey: String = "",
    override val subCategoryKey: String = "",
    override val mainCategoryType: MainCategoryType = MainCategoryType.TOP,
) : BaseFolderModel(), Parcelable

fun Folder.toDatabaseModel() =
    DatabaseFolder(
        key = key,
        name = name,
        parentKey = parentKey,
        subCategoryKey = subCategoryKey, mainCategoryType = mainCategoryType
    )

@Entity
data class DatabaseFolder(
    @PrimaryKey override val key: String = "",
    override val name: String = "",
    override val parentKey: String = "",
    override val subCategoryKey: String = "",
    override val createDate: Long = 0,
    override val editDate: Long = 0,
    override val mainCategoryType: MainCategoryType = MainCategoryType.TOP,
) : BaseDatabaseFolderModel() {
    override fun addCreateData(date: Long) = copy(createDate = date)

    override fun addEditDate() = copy(editDate = System.currentTimeMillis())

    override fun addKey(key: String) = copy(key = key)
}