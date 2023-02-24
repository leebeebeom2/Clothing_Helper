package com.leebeebeom.clothinghelper.data.datasourse.subcategory

import com.leebeebeom.clothinghelper.data.datasourse.BaseRoomDataSource
import com.leebeebeom.clothinghelper.domain.model.RoomSubCategory
import com.leebeebeom.clothinghelper.domain.model.SubCategory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubCategoryRoomDataSource @Inject constructor(private val dao: SubCategoryDao) :
    BaseRoomDataSource<RoomSubCategory, SubCategory>(dao = dao) {
    override fun getAll() = dao.getAll()
    override suspend fun deleteAll() = dao.deleteAll()
}