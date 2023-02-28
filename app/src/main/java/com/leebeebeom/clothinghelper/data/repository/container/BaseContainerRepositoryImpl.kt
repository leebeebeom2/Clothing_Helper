package com.leebeebeom.clothinghelper.data.repository.container

import com.leebeebeom.clothinghelper.data.repository.BaseDataRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.preference.Order.ASCENDING
import com.leebeebeom.clothinghelper.data.repository.preference.Order.DESCENDING
import com.leebeebeom.clothinghelper.data.repository.preference.Sort.*
import com.leebeebeom.clothinghelper.data.repository.preference.SortPreferences
import com.leebeebeom.clothinghelper.data.repository.util.NetworkChecker
import com.leebeebeom.clothinghelper.domain.model.BaseDatabaseContainerModel
import com.leebeebeom.clothinghelper.domain.repository.BaseContainerRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.NetworkPreferenceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn


abstract class BaseContainerRepositoryImpl<T : BaseDatabaseContainerModel>(
    sortFlow: Flow<SortPreferences>,
    refPath: String,
    networkPreferenceRepository: NetworkPreferenceRepository,
    appScope: CoroutineScope,
    networkChecker: NetworkChecker,
) : BaseContainerRepository<T>, BaseDataRepositoryImpl<T>(
    refPath = refPath,
    networkPreferences = networkPreferenceRepository,
    networkChecker = networkChecker
) {
    /**
     * TODO [allData] 혹은 sortFlow가 변경되면 업데이트 됨
     */
    override val allSortedData = combine(
        flow = super.allData,
        flow2 = sortFlow,
        transform = ::getSortedData
    ).stateIn(
        scope = appScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    private fun getSortedData(
        allData: List<T>,
        sortPreferences: SortPreferences,
    ): List<T> {
        val sort = sortPreferences.sort
        val order = sortPreferences.order

        return when {
            sort == NAME && order == ASCENDING -> allData.sortedBy { it.name }
            sort == NAME && order == DESCENDING -> allData.sortedByDescending { it.name }
            sort == CREATE && order == ASCENDING -> allData.sortedBy { it.createDate }
            sort == CREATE && order == DESCENDING -> allData.sortedByDescending { it.createDate }
            sort == EDIT && order == ASCENDING -> allData.sortedBy { it.editDate }
            sort == EDIT && order == DESCENDING -> allData.sortedByDescending { it.editDate }
            else -> throw Exception("정렬 정보 없음: sort: $sort, order: $order")
        }
    }
}