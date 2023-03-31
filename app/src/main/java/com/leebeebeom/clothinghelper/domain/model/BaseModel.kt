package com.leebeebeom.clothinghelper.domain.model

import com.leebeebeom.clothinghelper.ui.drawer.content.MainCategoryType

abstract class BaseModel {
    abstract val key: String
    abstract fun addKey(key: String): BaseModel
}

abstract class BaseContainerModel : BaseModel() {
    abstract val name: String
    abstract val createDate: Long
    abstract val editDate: Long
    abstract val mainCategoryType: MainCategoryType
    abstract fun changeEditDate(): BaseContainerModel
}

abstract class BaseFolderModel : BaseContainerModel() {
    abstract val parentKey: String
    abstract val subCategoryKey: String
}