package com.leebeebeom.clothinghelperdata.repository.container

import com.leebeebeom.clothinghelperdomain.model.Folder
import com.leebeebeom.clothinghelperdomain.repository.FolderRepository
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderRepositoryImpl @Inject constructor() : BaseContainerRepository<Folder>(),
    FolderRepository {
    override val allFolders get() = allContainers.asStateFlow()

    override suspend fun loadFolders(uid: String) = load(uid, Folder::class.java)

    override suspend fun addFolder(folder: Folder) = add(folder)

    override suspend fun editFolderName(newFolder: Folder) = edit(newFolder)

    override val refPath = DatabasePath.FOLDERS

    override fun getContainerWithKey(value: Folder, key: String, createDate: Long) =
        value.copy(key = key, createDate = createDate)

    override fun getContainerWithKey(value: Folder, key: String) = value.copy(key = key)
}