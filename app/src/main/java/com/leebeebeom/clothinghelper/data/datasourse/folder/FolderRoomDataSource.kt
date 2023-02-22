package com.leebeebeom.clothinghelper.data.datasourse.folder

import com.leebeebeom.clothinghelper.data.datasourse.BaseRoomDataSource
import com.leebeebeom.clothinghelper.domain.model.data.Folder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderRoomDataSource @Inject constructor(private val dao: FolderDao) :
    BaseRoomDataSource<Folder>() {

    override fun getAll() = dao.getAll()
    override suspend fun deleteAll() = dao.deleteAll()
    override suspend fun insert(data: Folder) = dao.insert(data)
    override suspend fun insert(data: List<Folder>) = dao.insert(data)
    override suspend fun update(data: Folder) = dao.update(data)
}