package com.leebeebeom.clothinghelper.domain.model.data

import com.leebeebeom.clothinghelper.ui.main.drawer.MainCategoryType

abstract class BaseModel {
    abstract val parent: MainCategoryType
    abstract val name: String
    abstract val key: String
    abstract val createDate: Long
    abstract val editDate: Long

    abstract fun addKey(key: String): BaseModel
}

abstract class BaseFolderModel : BaseModel() {
    abstract val parentKey: String
    abstract val subCategoryKey: String
}