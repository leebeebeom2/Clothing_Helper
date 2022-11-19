package com.leebeebeom.clothinghelper.map

import com.leebeebeom.clothinghelperdomain.model.container.Folder

data class StableFolder(
    val parentKey: String = "",
    val createDate: Long = 0,
    val name: String = "",
    val key: String = "",
    val editDate: Long = 0
)

fun Folder.toStable() = StableFolder(parentKey, createDate, name, key, editDate)

fun StableFolder.toStable() = Folder(parentKey, createDate, name, key, editDate)