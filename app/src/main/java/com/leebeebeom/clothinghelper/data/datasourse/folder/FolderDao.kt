package com.leebeebeom.clothinghelper.data.datasourse.folder

import androidx.room.Dao
import androidx.room.Query
import com.leebeebeom.clothinghelper.data.datasourse.BaseDao
import com.leebeebeom.clothinghelper.domain.model.data.Folder
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao : BaseDao<Folder> {
    @Query("SELECT * FROM Folder")
    fun getAll(): Flow<List<Folder>>

    @Query("SELECT * FROM Folder WHERE isSynced = false")
    suspend fun getAllAsync(): List<Folder>

    @Query("DELETE FROM Folder")
    suspend fun deleteAll()
}