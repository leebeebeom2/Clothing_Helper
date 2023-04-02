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
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
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
), FolderRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override val folderNamesMapFlow = allDataFlow.mapLatest {
        it.data.groupBy { element -> element.parentKey }
            .mapValues { mapElement ->
                mapElement.value.map { element -> element.name }.toImmutableSet()
            }
            .toImmutableMap()
    }.flowOn(dispatcherDefault)
        .distinctUntilChanged()
        .shareIn(scope = appScope, started = SharingStarted.WhileSubscribed(5000), replay = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val folderSizeMapFlow = allDataFlow.mapLatest {
        it.data.groupingBy { element -> element.parentKey }.eachCount().toImmutableMap()
    }.flowOn(dispatcherDefault)
        .distinctUntilChanged()
        .shareIn(scope = appScope, started = SharingStarted.WhileSubscribed(5000), replay = 1)
}