package com.leebeebeom.clothinghelper.data.datasourse.subcategory

import androidx.room.*
import com.leebeebeom.clothinghelper.data.datasourse.BaseDao
import com.leebeebeom.clothinghelper.domain.model.RoomSubCategory
import com.leebeebeom.clothinghelper.domain.model.SubCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface SubCategoryDao : BaseDao<RoomSubCategory> {
    @Query("SELECT `key`, name, parent FROM RoomSubCategory")
    fun getAll(): Flow<List<SubCategory>>

    @Query("DELETE FROM RoomSubCategory")
    suspend fun deleteAll()
}