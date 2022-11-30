package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.data.Folder
import kotlinx.coroutines.flow.Flow

interface FolderRepository : ContainerRepository<Folder> {
    val allFolders: Flow<List<Folder>>
}