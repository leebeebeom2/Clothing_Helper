package com.leebeebeom.clothinghelper.main.base

import com.leebeebeom.clothinghelper.base.BaseUIState
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.User

abstract class BaseSubCategoryUIState : BaseUIState() {
    abstract val user: User?
    abstract val isLoading: Boolean
    abstract val allSubCategories: List<List<SubCategory>>
}