package com.leebeebeom.clothinghelper.data.datasourse.folder

import com.leebeebeom.clothinghelper.data.datasourse.BaseRoomDataSource
import com.leebeebeom.clothinghelper.domain.model.data.Folder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderRoomDataSource @Inject constructor(dao: FolderDao) :
    BaseRoomDataSource<Folder>(dao = dao)