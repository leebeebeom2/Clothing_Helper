package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.model.container.Folder
import kotlinx.coroutines.flow.Flow

interface FolderRepository {
    val allFolders: Flow<List<Folder>>
    val isLoading: Flow<Boolean>

    suspend fun loadFolders(uid: String?): FirebaseResult
    suspend fun addFolder(folder: Folder, uid: String): FirebaseResult
    suspend fun editFolderName(newFolder: Folder, uid: String): FirebaseResult
}