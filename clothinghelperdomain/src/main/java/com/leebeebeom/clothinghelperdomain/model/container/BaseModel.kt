package com.leebeebeom.clothinghelperdomain.model.container

abstract class BaseModel {
    abstract val parent: SubCategoryParent
    abstract val name: String
    abstract val key: String
    abstract val createDate: Long
    abstract val editDate: Long
}