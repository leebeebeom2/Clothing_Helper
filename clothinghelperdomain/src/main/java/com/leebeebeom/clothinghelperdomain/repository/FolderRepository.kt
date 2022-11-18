package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.model.container.Folder
import kotlinx.coroutines.flow.StateFlow

interface FolderRepository {
    val allFolders: StateFlow<List<Folder>>

    suspend fun loadFolders(uid: String): FirebaseResult
    suspend fun addFolder(folder: Folder, uid: String): FirebaseResult
    suspend fun editFolderName(newFolder: Folder, uid: String): FirebaseResult
}