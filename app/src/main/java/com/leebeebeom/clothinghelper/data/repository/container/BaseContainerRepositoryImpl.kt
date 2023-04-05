package com.leebeebeom.clothinghelper.data.repository.container

import com.leebeebeom.clothinghelper.data.repository.BaseDataRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.DataBasePath
import com.leebeebeom.clothinghelper.data.repository.preference.Order.Ascending
import com.leebeebeom.clothinghelper.data.repository.preference.Order.Descending
import com.leebeebeom.clothinghelper.data.repository.preference.Sort.*
import com.leebeebeom.clothinghelper.data.repository.preference.SortPreferences
import com.leebeebeom.clothinghelper.domain.model.BaseContainerModel
import com.leebeebeom.clothinghelper.domain.repository.*
import kotlinx.collections.immutable.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*


@OptIn(ExperimentalCoroutinesApi::class)
@Suppress("UNCHECKED_CAST")
abstract class BaseContainerRepositoryImpl<T : BaseContainerModel, K>(
    sortFlow: Flow<SortPreferences>,
    refPath: DataBasePath,
    appScope: CoroutineScope,
    type: Class<T>,
    dispatcherIO: CoroutineDispatcher,
    dispatcherDefault: CoroutineDispatcher,
    userRepository: UserRepository,
) : BaseContainerRepository<T, K>, BaseDataRepositoryImpl<T>(
    refPath = refPath,
    appScope = appScope,
    type = type,
    dispatcherIO = dispatcherIO,
    userRepository = userRepository
) {
    final override val allDataFlow =
        super.allDataFlow.combine(flow = sortFlow, transform = ::getSortedData)
            .flowOn(dispatcherDefault)
            .distinctUntilChanged()
            .shareIn(scope = appScope, started = SharingStarted.WhileSubscribed(5000), replay = 1)

    private fun getSortedData(
        dataResult: DataResult<T>,
        sortPreferences: SortPreferences,
    ) = when (dataResult) {
        is DataResult.Fail -> dataResult
        is DataResult.Success -> {
            val allData = dataResult.data
            val sort = sortPreferences.sort
            val order = sortPreferences.order

            val sortedList = when {
                sort == Name && order == Ascending -> allData.sortedBy { it.name }
                sort == Name && order == Descending -> allData.sortedByDescending { it.name }
                sort == Create && order == Ascending -> allData.sortedBy { it.createDate }
                sort == Create && order == Descending -> allData.sortedByDescending { it.createDate }
                sort == Edit && order == Ascending -> allData.sortedBy { it.editDate }
                sort == Edit && order == Descending -> allData.sortedByDescending { it.editDate }
                else -> allData
            }

            DataResult.Success(sortedList.toImmutableList())
        }
    }

    override suspend fun add(data: T) {
        // push override 때문에 editDate 변경 됨
        val dataWithKey = data.addKey(key = getKey()) as T

        super.push(dataWithKey)
    }

    override suspend fun push(data: T) = super.push(data = data.changeEditDate() as T)

    abstract fun groupByKeySelector(element: T): K

    final override val allDataMapFlow: SharedFlow<DataResultMap<K, T>> =
        allDataFlow.mapLatest { dataResult ->
            dataResult.toMap(::groupByKeySelector)
        }.flowOn(dispatcherDefault)
            .distinctUntilChanged()
            .shareIn(scope = appScope, started = SharingStarted.WhileSubscribed(5000), replay = 1)

    override val dataNamesMapFlow = allDataMapFlow.mapLatest { allDataMap ->
        allDataMap.data.mapValues { mapElement ->
            mapElement.value.map { element -> element.name }.toImmutableSet()
        }.toImmutableMap()
    }.flowOn(dispatcherDefault)
        .distinctUntilChanged()
        .shareIn(scope = appScope, started = SharingStarted.WhileSubscribed(5000), replay = 1)

    override val dataSizeMapFlow = allDataMapFlow.mapLatest { allDataMap ->
        allDataMap.data.mapValues { mapElement -> mapElement.value.size }.toImmutableMap()
    }.flowOn(dispatcherDefault)
        .distinctUntilChanged()
        .shareIn(scope = appScope, started = SharingStarted.WhileSubscribed(5000), replay = 1)
}