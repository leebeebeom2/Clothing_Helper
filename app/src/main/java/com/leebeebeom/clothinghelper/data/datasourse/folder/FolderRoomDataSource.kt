package com.leebeebeom.clothinghelper.data.datasourse.folder

import com.leebeebeom.clothinghelper.data.datasourse.BaseRoomDataSource
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.model.DatabaseFolder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderRoomDataSource @Inject constructor(private val dao: FolderDao) :
    BaseRoomDataSource<DatabaseFolder, Folder>(dao = dao) {
    override fun getAll() = dao.getAll()
    override suspend fun deleteAll() = dao.deleteAll()
}