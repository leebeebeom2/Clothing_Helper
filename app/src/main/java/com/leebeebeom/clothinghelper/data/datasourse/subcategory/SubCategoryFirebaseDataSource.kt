package com.leebeebeom.clothinghelper.data.datasourse.subcategory

import com.leebeebeom.clothinghelper.data.datasourse.BaseFirebaseDataSource
import com.leebeebeom.clothinghelper.data.repository.DatabasePath
import com.leebeebeom.clothinghelper.domain.model.data.SubCategory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubCategoryFirebaseDataSource @Inject constructor() :
    BaseFirebaseDataSource<SubCategory>(refPath = DatabasePath.SUB_CATEGORIES)