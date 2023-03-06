package com.leebeebeom.clothinghelper.data.repository.container

import com.leebeebeom.clothinghelper.data.repository.DatabasePath
import com.leebeebeom.clothinghelper.data.repository.util.NetworkChecker
import com.leebeebeom.clothinghelper.di.AppScope
import com.leebeebeom.clothinghelper.di.DispatcherIO
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.FolderPreferencesRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderRepositoryImpl @Inject constructor(
    @FolderPreferencesRepository folderPreferencesRepository: SortPreferenceRepository,
    networkChecker: NetworkChecker,
    @AppScope appScope: CoroutineScope,
    @DispatcherIO dispatcher: CoroutineDispatcher,
) : BaseContainerRepositoryImpl<Folder>(
    sortFlow = folderPreferencesRepository.sort,
    refPath = DatabasePath.FOLDERS,
    networkChecker = networkChecker,
    appScope = appScope,
    type = Folder::class.java,
    dispatcher = dispatcher
), FolderRepository