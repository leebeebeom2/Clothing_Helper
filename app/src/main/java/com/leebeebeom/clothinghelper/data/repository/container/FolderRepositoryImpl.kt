package com.leebeebeom.clothinghelper.data.repository.container

import com.leebeebeom.clothinghelper.data.repository.DataBasePath
import com.leebeebeom.clothinghelper.di.AppScope
import com.leebeebeom.clothinghelper.di.DispatcherDefault
import com.leebeebeom.clothinghelper.di.DispatcherIO
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.FolderPreferencesRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderRepositoryImpl @Inject constructor(
    @FolderPreferencesRepository folderPreferencesRepository: SortPreferenceRepository,
    @AppScope appScope: CoroutineScope,
    @DispatcherIO dispatcherIO: CoroutineDispatcher,
    @DispatcherDefault dispatcherDefault: CoroutineDispatcher,
    userRepository: UserRepository,
) : BaseContainerRepositoryImpl<Folder>(
    sortFlow = folderPreferencesRepository.sortFlow,
    refPath = DataBasePath.Folder,
    appScope = appScope,
    type = Folder::class.java,
    dispatcherIO = dispatcherIO,
    dispatcherDefault = dispatcherDefault,
    userRepository = userRepository,
), FolderRepository