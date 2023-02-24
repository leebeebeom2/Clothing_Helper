package com.leebeebeom.clothinghelper.data.datasourse.folder

import com.leebeebeom.clothinghelper.data.datasourse.BaseFirebaseDataSource
import com.leebeebeom.clothinghelper.data.repository.DatabasePath
import com.leebeebeom.clothinghelper.domain.model.FirebaseFolder
import com.leebeebeom.clothinghelper.domain.model.Folder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderFirebaseDataSource @Inject constructor() : BaseFirebaseDataSource<FirebaseFolder>(
    refPath = DatabasePath.FOLDERS
)