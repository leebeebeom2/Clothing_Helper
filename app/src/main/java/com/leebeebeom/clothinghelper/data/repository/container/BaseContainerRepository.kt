package com.leebeebeom.clothinghelper.data.repository.container

import android.content.Context
import com.leebeebeom.clothinghelper.data.datasourse.BaseFirebaseDataSource
import com.leebeebeom.clothinghelper.data.datasourse.BaseRoomDataSource
import com.leebeebeom.clothinghelper.data.repository.preference.NetworkPreferenceRepository
import com.leebeebeom.clothinghelper.data.repository.preference.Order.ASCENDING
import com.leebeebeom.clothinghelper.data.repository.preference.Order.DESCENDING
import com.leebeebeom.clothinghelper.data.repository.preference.Sort.*
import com.leebeebeom.clothinghelper.data.repository.preference.SortPreferences
import com.leebeebeom.clothinghelper.domain.model.data.BaseContainerModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine


abstract class BaseContainerRepository<T : BaseContainerModel>(
    sortFlow: Flow<SortPreferences>,
    context: Context,
    refPath: String,
    baseFirebaseDataSource: BaseFirebaseDataSource<T>,
    baseRoomDataSource: BaseRoomDataSource<T>,
    networkPreferences: NetworkPreferenceRepository,
) : BaseDataRepositoryImpl<T>(
    context = context,
    refPath = refPath,
    baseFirebaseDataSource = baseFirebaseDataSource,
    baseRoomDataSource = baseRoomDataSource,
    networkPreferences = networkPreferences
) {
    /**
     * [allData] 혹은 [sortFlow]가 변경되면 업데이트 됨
     */
    override val allData = combine(flow = baseRoomDataSource.getAll(), flow2 = sortFlow, transform = ::getSortedData) // TODO 되나?

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