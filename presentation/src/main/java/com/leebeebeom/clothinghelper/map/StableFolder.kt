package com.leebeebeom.clothinghelper.map

import com.leebeebeom.clothinghelperdomain.model.data.Folder
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent

data class StableFolder(
    val parentKey: String = "",
    val subCategoryKey: String = "",
    override val createDate: Long = 0,
    override val name: String = "",
    override val key: String = "",
    override val editDate: Long = 0,
    override val parent: SubCategoryParent = SubCategoryParent.TOP
) : BaseStableModel()

fun Folder.toStable() = StableFolder(
    parentKey = parentKey,
    subCategoryKey = subCategoryKey,
    createDate = createDate,
    name = name,
    key = key,
    editDate = editDate,
    parent = parent
)

fun StableFolder.toUnstable() = Folder(
    parentKey = parentKey,
    subCategoryKey = subCategoryKey,
    createDate = createDate,
    name = name,
    key = key,
    editDate = editDate,
    parent = parent
)