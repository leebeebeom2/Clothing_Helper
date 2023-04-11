package com.leebeebeom.clothinghelper.data.repository

import com.leebeebeom.clothinghelper.data.repository.preference.Order
import com.leebeebeom.clothinghelper.data.repository.preference.Sort
import com.leebeebeom.clothinghelper.data.repository.preference.SortPreferences
import com.leebeebeom.clothinghelper.di.AppScope
import com.leebeebeom.clothinghelper.di.DispatcherDefault
import com.leebeebeom.clothinghelper.di.DispatcherIO
import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.repository.*
import com.leebeebeom.clothinghelper.domain.repository.preference.FolderPreferenceRepository
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class FolderRepositoryImpl @Inject constructor(
    folderPreferencesRepository: FolderPreferenceRepository,
    @AppScope appScope: CoroutineScope,
    @DispatcherIO dispatcherIO: CoroutineDispatcher,
    @DispatcherDefault dispatcherDefault: CoroutineDispatcher,
    userRepository: UserRepository,
) : BaseDataRepositoryImpl<Folder>(
    refPath = DataBasePath.Folder,
    appScope = appScope,
    type = Folder::class.java,
    dispatcherIO = dispatcherIO,
    userRepository = userRepository
), FolderRepository {

    override val allDataFlow = super.allDataFlow.combine(
        flow = folderPreferencesRepository.sortFlow, transform = ::getSortedFolder
    ).flowOn(dispatcherDefault).distinctUntilChanged()
        .shareIn(scope = appScope, started = SharingStarted.WhileSubscribed(5000), replay = 1)

    override suspend fun add(data: Folder) {
        // push override 때문에 editDate 변경 됨
        val dataWithKey = data.addKey(key = getKey())

        super.push(dataWithKey)
    }

    override suspend fun push(data: Folder) = super.push(data = data.changeEditDate())

    override val allFoldersMapFlow: SharedFlow<FolderResultMap> =
        allDataFlow.mapLatest { dataResult ->
            dataResult.toFolderResultMap { it.parentKey }
        }.flowOn(dispatcherDefault).distinctUntilChanged()
            .shareIn(scope = appScope, started = SharingStarted.WhileSubscribed(5000), replay = 1)

    override val folderNamesMapFlow = allFoldersMapFlow.mapLatest { allDataMap ->
        allDataMap.data.mapValues { mapElement ->
            mapElement.value.map { element -> element.name }.toImmutableSet()
        }.toImmutableMap()
    }.flowOn(dispatcherDefault).distinctUntilChanged()
        .shareIn(scope = appScope, started = SharingStarted.WhileSubscribed(5000), replay = 1)

    override val foldersSizeMapFlow = allFoldersMapFlow.mapLatest { allDataMap ->
        allDataMap.data.mapValues { mapElement -> mapElement.value.size }.toImmutableMap()
    }.flowOn(dispatcherDefault).distinctUntilChanged()
        .shareIn(scope = appScope, started = SharingStarted.WhileSubscribed(5000), replay = 1)

    private fun getSortedFolder(
        dataResult: DataResult<Folder>,
        sortPreferences: SortPreferences,
    ) = when (dataResult) {
        is DataResult.Fail -> dataResult
        is DataResult.Success -> {
            val allData = dataResult.data
            val sort = sortPreferences.sort
            val order = sortPreferences.order

            val sortedList = when {
                sort == Sort.Name && order == Order.Ascending -> allData.sortedBy { it.name }
                sort == Sort.Name && order == Order.Descending -> allData.sortedByDescending { it.name }
                sort == Sort.Create && order == Order.Ascending -> allData.sortedBy { it.createDate }
                sort == Sort.Create && order == Order.Descending -> allData.sortedByDescending { it.createDate }
                sort == Sort.Edit && order == Order.Ascending -> allData.sortedBy { it.editDate }
                sort == Sort.Edit && order == Order.Descending -> allData.sortedByDescending { it.editDate }
                else -> allData
            }

            DataResult.Success(sortedList.toImmutableList())
        }
    }
}