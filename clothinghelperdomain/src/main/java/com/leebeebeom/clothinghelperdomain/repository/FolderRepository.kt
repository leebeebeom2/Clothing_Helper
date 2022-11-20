package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.container.Folder
import kotlinx.coroutines.flow.Flow

interface FolderRepository : LoadingRepository, ContainerRepository<Folder> {
    val allFolders: Flow<List<Folder>>
}