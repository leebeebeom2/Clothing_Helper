package com.leebeebeom.clothinghelper.data.datasourse.subcategory

import androidx.room.*
import com.leebeebeom.clothinghelper.data.datasourse.BaseDao
import com.leebeebeom.clothinghelper.domain.model.DatabaseSubCategory
import com.leebeebeom.clothinghelper.domain.model.SubCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface SubCategoryDao : BaseDao<DatabaseSubCategory> {
    @Query("SELECT `key`, name, parent FROM DatabaseSubCategory")
    fun getAll(): Flow<List<SubCategory>>

    @Query("DELETE FROM DatabaseSubCategory")
    suspend fun deleteAll()
}