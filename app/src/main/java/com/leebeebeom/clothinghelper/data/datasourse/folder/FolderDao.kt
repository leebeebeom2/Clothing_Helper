package com.leebeebeom.clothinghelper.data.datasourse.folder

import androidx.room.Dao
import androidx.room.Query
import com.leebeebeom.clothinghelper.data.datasourse.BaseDao
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.model.DatabaseFolder
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao : BaseDao<DatabaseFolder> {
    @Query("SELECT `key`,name,parentKey,subCategoryKey FROM DatabaseFolder")
    fun getAll(): Flow<List<Folder>>

    @Query("DELETE FROM DatabaseFolder")
    suspend fun deleteAll()
}