package com.leebeebeom.clothinghelper.domain.repository

import com.leebeebeom.clothinghelper.domain.model.data.Folder
import com.leebeebeom.clothinghelper.util.LoadingStateProvider

interface FolderRepository : BaseDataRepository<Folder>, LoadingStateProvider