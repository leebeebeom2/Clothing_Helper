package com.leebeebeom.clothinghelper.data.datasourse.subcategory

import com.leebeebeom.clothinghelper.data.datasourse.BaseRoomDataSource
import com.leebeebeom.clothinghelper.domain.model.data.SubCategory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubCategoryRoomDataSource @Inject constructor(private val dao: SubCategoryDao) :
    BaseRoomDataSource<SubCategory>() {

    override fun getAll() = dao.getAll()
    override suspend fun deleteAll() = dao.deleteAll()
    override suspend fun insert(data: SubCategory) = dao.insert(data)
    override suspend fun insert(data: List<SubCategory>) = dao.insert(data)
    override suspend fun update(data: SubCategory) = dao.update(data)
}