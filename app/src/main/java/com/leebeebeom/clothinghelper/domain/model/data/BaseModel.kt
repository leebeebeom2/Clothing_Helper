package com.leebeebeom.clothinghelper.domain.model.data

import com.leebeebeom.clothinghelper.ui.main.drawer.MainCategoryType

abstract class BaseModel {
    abstract val key: String
    abstract val isSynced: Boolean
    abstract fun addKey(key: String): BaseModel
    abstract fun synced(): BaseModel
}

abstract class BaseContainerModel : BaseModel() {
    abstract val parent: MainCategoryType
    abstract val name: String
    abstract val createDate: Long
    abstract val editDate: Long
}

abstract class BaseFolderModel : BaseContainerModel() {
    abstract val parentKey: String
    abstract val subCategoryKey: String
}