package com.leebeebeom.clothinghelper.data.repository.container

import com.google.firebase.FirebaseNetworkException
import com.leebeebeom.clothinghelper.data.repository.BaseDataRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.preference.Order.ASCENDING
import com.leebeebeom.clothinghelper.data.repository.preference.Order.DESCENDING
import com.leebeebeom.clothinghelper.data.repository.preference.Sort.*
import com.leebeebeom.clothinghelper.data.repository.preference.SortPreferences
import com.leebeebeom.clothinghelper.data.repository.util.NetworkChecker
import com.leebeebeom.clothinghelper.data.repository.util.WifiException
import com.leebeebeom.clothinghelper.domain.model.BaseContainerModel
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine


@Suppress("UNCHECKED_CAST")
abstract class BaseContainerRepositoryImpl<T : BaseContainerModel>(
    private val sortFlow: Flow<SortPreferences>,
    refPath: String,
    networkChecker: NetworkChecker,
) : BaseDataRepository<T>, BaseDataRepositoryImpl<T>(
    refPath = refPath, networkChecker = networkChecker
) {
    override suspend fun getAllData(
        dispatcher: CoroutineDispatcher,
        uid: String?,
        type: Class<T>,
        onFail: (Exception) -> Unit,
    ): Flow<List<T>> {
        val allDataFLow = super.getAllData(
            dispatcher = dispatcher, uid = uid, type = type, onFail = onFail
        )

        return allDataFLow.combine(flow = sortFlow, transform = ::getSortedData)
    }

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

    /**
     * @throws FirebaseNetworkException 인터넷 미 연결 시
     * @throws WifiException 사용자가 와이파이로만 연결 선택 시 와이파이 미 연결됐을 경우
     * @throws NoSuchElementException 본래 데이터를 찾지 못했을 경우
     * @throws IllegalArgumentException 본래 데이터를 삭제하지 못했을 경우
     */
    override suspend fun edit(
        dispatcher: CoroutineDispatcher,
        newData: T,
        uid: String,
        onFail: (Exception) -> Unit,
    ) {
        val newDataWithEditDate = newData.changeEditDate() as T

        super.edit(
            dispatcher = dispatcher, newData = newDataWithEditDate, uid = uid, onFail = onFail
        )
    }
}