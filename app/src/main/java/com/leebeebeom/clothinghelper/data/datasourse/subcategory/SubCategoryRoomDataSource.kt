package com.leebeebeom.clothinghelper.data.datasourse.subcategory

import com.leebeebeom.clothinghelper.data.datasourse.BaseRoomDataSource
import com.leebeebeom.clothinghelper.domain.model.data.SubCategory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubCategoryRoomDataSource @Inject constructor(dao: SubCategoryDao) :
    BaseRoomDataSource<SubCategory>(dao = dao)