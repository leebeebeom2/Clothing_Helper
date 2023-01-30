package com.leebeebeom.clothinghelper.ui.util

import com.leebeebeom.clothinghelper.domain.model.data.Folder
import com.leebeebeom.clothinghelper.domain.model.data.SubCategory
import com.leebeebeom.clothinghelper.domain.model.data.SubCategoryParent

typealias AddSubCategory = (name: String, parent: SubCategoryParent, showToast: ShowToast) -> Unit
typealias EditSubCategory = (oldSubCategory: SubCategory, name: String, showToast: ShowToast) -> Unit

typealias AddFolder = (parentKey: String, subCategoryKey: String, name: String, parent: SubCategoryParent, showToast: ShowToast) -> Unit
typealias EditFolder = (oldFolder: Folder, name: String, showToast: ShowToast) -> Unit

typealias ShowToast = (toastText: Int) -> Unit