package com.leebeebeom.clothinghelperdata.repository.container

import com.leebeebeom.clothinghelperdata.repository.base.DatabasePath
import com.leebeebeom.clothinghelperdomain.model.container.Folder
import com.leebeebeom.clothinghelperdomain.repository.FolderRepository
import com.leebeebeom.clothinghelperdomain.repository.preferences.FolderPreferencesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderRepositoryImpl @Inject constructor(
    folderPreferencesRepository: FolderPreferencesRepository
) : ContainerRepository<Folder>(),
    FolderRepository {
    override val allFolders = getSortedContainers(folderPreferencesRepository.sort)

    override suspend fun loadFolders(uid: String?) = load(uid, Folder::class.java)

    override suspend fun addFolder(folder: Folder, uid: String) = add(folder, uid)

    override suspend fun editFolderName(newFolder: Folder, uid: String) = edit(newFolder, uid)

    override val refPath = DatabasePath.FOLDERS

    override fun getNewContainer(value: Folder, key: String, createDate: Long) =
        value.copy(key = key, createDate = createDate, editDate = createDate)

    override fun getContainerWithNewEditDate(value: Folder, editDate: Long) =
        value.copy(editDate = editDate)
}