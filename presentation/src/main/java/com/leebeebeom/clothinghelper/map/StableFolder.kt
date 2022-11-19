package com.leebeebeom.clothinghelper.map

import com.leebeebeom.clothinghelperdomain.model.container.BaseContainer
import com.leebeebeom.clothinghelperdomain.model.container.Folder

data class StableFolder(
    val parentKey: String = "",
    override val createDate: Long = 0,
    override val name: String = "",
    override val key: String = "",
    override val editDate: Long = 0
) : BaseContainer()

fun Folder.toStable() = StableFolder(parentKey, createDate, name, key, editDate)

fun StableFolder.toStable() = Folder(parentKey, createDate, name, key, editDate)