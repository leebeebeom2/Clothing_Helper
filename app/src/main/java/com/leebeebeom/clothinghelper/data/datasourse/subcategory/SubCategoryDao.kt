package com.leebeebeom.clothinghelper.data.datasourse.subcategory

import androidx.room.*
import com.leebeebeom.clothinghelper.data.datasourse.BaseDao
import com.leebeebeom.clothinghelper.domain.model.data.SubCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface SubCategoryDao : BaseDao<SubCategory> {
    @Query("SELECT * FROM SubCategory")
    fun getAll(): Flow<List<SubCategory>>

    @Query("SELECT * FROM SubCategory WHERE isSynced = false")
    suspend fun getAllAsync(): List<SubCategory>

    @Query("DELETE FROM SubCategory")
    suspend fun deleteAll()
}