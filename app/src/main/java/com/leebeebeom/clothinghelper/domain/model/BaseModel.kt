package com.leebeebeom.clothinghelper.domain.model

abstract class BaseModel {
    abstract val key: String
    abstract fun addKey(key: String): BaseModel
}

abstract class BaseBrandModel : BaseModel() {
    abstract val name: String
    abstract val url: String
}