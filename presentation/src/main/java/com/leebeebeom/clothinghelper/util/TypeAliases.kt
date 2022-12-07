package com.leebeebeom.clothinghelper.util

import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent

typealias AddSubCategory = (name: String, parent: SubCategoryParent) -> Unit
typealias EditSubCategory = (oldSubCategory: StableSubCategory, name: String) -> Unit

typealias AddFolder = (parentKey: String, subCategoryKey: String, name: String, parent: SubCategoryParent) -> Unit
typealias EditFolder = (oldFolder: StableFolder, name: String) -> Unit