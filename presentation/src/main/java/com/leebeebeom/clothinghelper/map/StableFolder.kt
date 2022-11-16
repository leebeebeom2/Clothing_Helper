package com.leebeebeom.clothinghelper.map

import com.leebeebeom.clothinghelperdomain.model.Folder

data class StableFolder(
    val parentKey: String = "", val name: String = ""
)

fun Folder.toStable(): StableFolder {
    return StableFolder(parentKey, name)
}