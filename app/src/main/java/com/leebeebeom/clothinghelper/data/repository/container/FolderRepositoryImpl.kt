package com.leebeebeom.clothinghelper.data.repository.container

import android.content.Context
import com.leebeebeom.clothinghelper.data.datasourse.folder.FolderFirebaseDataSource
import com.leebeebeom.clothinghelper.data.datasourse.folder.FolderRoomDataSource
import com.leebeebeom.clothinghelper.data.repository.DatabasePath
import com.leebeebeom.clothinghelper.data.repository.preference.NetworkPreferenceRepository
import com.leebeebeom.clothinghelper.domain.model.data.Folder
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.FolderPreferencesRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderRepositoryImpl @Inject constructor(
    @FolderPreferencesRepository folderPreferencesRepository: SortPreferenceRepository,
    @ApplicationContext context: Context,
    folderRoomDataSource: FolderRoomDataSource,
    folderFirebaseDataSource: FolderFirebaseDataSource,
    networkPreferenceRepository: NetworkPreferenceRepository,
) : BaseContainerRepository<Folder>(
    sortFlow = folderPreferencesRepository.sort,
    refPath = DatabasePath.FOLDERS,
    context = context,
    baseFirebaseDataSource = folderFirebaseDataSource,
    baseRoomDataSource = folderRoomDataSource,
    networkPreferenceRepository = networkPreferenceRepository
), FolderRepository