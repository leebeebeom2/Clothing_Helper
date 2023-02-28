package com.leebeebeom.clothinghelper.data.repository.container

import com.leebeebeom.clothinghelper.data.repository.DatabasePath
import com.leebeebeom.clothinghelper.data.repository.util.NetworkChecker
import com.leebeebeom.clothinghelper.di.AppScope
import com.leebeebeom.clothinghelper.domain.model.DatabaseFolder
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.FolderPreferencesRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.NetworkPreferenceRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderRepositoryImpl @Inject constructor(
    @FolderPreferencesRepository folderPreferencesRepository: SortPreferenceRepository,
    networkPreferenceRepository: NetworkPreferenceRepository,
    @AppScope appScope: CoroutineScope,
    networkChecker: NetworkChecker,
) : BaseContainerRepositoryImpl<DatabaseFolder>(
    sortFlow = folderPreferencesRepository.sort,
    refPath = DatabasePath.FOLDERS,
    networkPreferenceRepository = networkPreferenceRepository,
    appScope = appScope,
    networkChecker = networkChecker
), FolderRepository