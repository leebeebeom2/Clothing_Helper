package com.leebeebeom.clothinghelper.data.datasourse.subcategory

import androidx.room.Dao
import androidx.room.Query
import com.leebeebeom.clothinghelper.data.datasourse.BaseDao
import com.leebeebeom.clothinghelper.domain.model.data.SubCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface SubCategoryDao : BaseDao<SubCategory> {
    @Query("SELECT * FROM SubCategory")
    override fun getAll(): Flow<List<SubCategory>>

    @Query("DELETE FROM SubCategory")
    suspend fun deleteAll()
}