package com.leebeebeom.clothinghelper.map

import com.leebeebeom.clothinghelperdomain.model.container.Folder

data class StableFolder(val parentKey: String = "", val name: String = "")

fun Folder.toStable() = StableFolder(parentKey, name)