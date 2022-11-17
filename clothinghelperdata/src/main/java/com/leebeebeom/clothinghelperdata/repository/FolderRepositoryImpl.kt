package com.leebeebeom.clothinghelperdata.repository

import com.leebeebeom.clothinghelperdata.repository.base.BaseContainerRepository
import com.leebeebeom.clothinghelperdata.repository.base.DatabasePath
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.model.Folder
import com.leebeebeom.clothinghelperdomain.repository.FolderRepository
import kotlinx.coroutines.flow.asStateFlow

class FolderRepositoryImpl : BaseContainerRepository<Folder>(), FolderRepository {
    override val allFolders get() = allContainers.asStateFlow()

    override suspend fun updateFolders(uid: String): FirebaseResult {
        return load(uid, Folder::class.java)
    }

    override suspend fun addFolder(folder: Folder): FirebaseResult {
        return add(folder)
    }

    override suspend fun editFolderName(newFolder: Folder): FirebaseResult {
        return edit(newFolder)
    }

    override val refPath = DatabasePath.FOLDERS

    override fun getContainerWithKey(value: Folder, key: String, createDate: Long): Folder {
        return value.copy(key = key, createDate = createDate)
    }

    override fun getContainerWithKey(value: Folder, key: String): Folder {
        return value.copy(key = key)
    }
}