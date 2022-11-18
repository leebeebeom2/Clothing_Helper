package com.leebeebeom.clothinghelperdata.repository.container

import com.leebeebeom.clothinghelperdomain.model.container.Folder
import com.leebeebeom.clothinghelperdomain.repository.FolderRepository
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderRepositoryImpl @Inject constructor() : BaseContainerRepository<Folder>(),
    FolderRepository {
    override val allFolders get() = allContainers.asStateFlow()

    override suspend fun loadFolders(uid: String) = load(uid, Folder::class.java)

    override suspend fun addFolder(folder: Folder, uid: String) = add(folder, uid)

    override suspend fun editFolderName(newFolder: Folder, uid: String) = edit(newFolder, uid)

    override val refPath = DatabasePath.FOLDERS

    override fun getNewContainer(value: Folder, key: String, date: Long) =
        value.copy(key = key, createDate = date, editDate = date)

    override fun getContainerWithNewEditDate(value: Folder, editDate: Long) =
        value.copy(editDate = editDate)
}