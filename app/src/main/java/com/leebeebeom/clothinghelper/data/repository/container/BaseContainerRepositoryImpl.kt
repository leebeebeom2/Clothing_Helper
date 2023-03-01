package com.leebeebeom.clothinghelper.data.repository.container

import com.google.firebase.FirebaseNetworkException
import com.leebeebeom.clothinghelper.data.repository.BaseDataRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.preference.Order.ASCENDING
import com.leebeebeom.clothinghelper.data.repository.preference.Order.DESCENDING
import com.leebeebeom.clothinghelper.data.repository.preference.Sort.*
import com.leebeebeom.clothinghelper.data.repository.preference.SortPreferences
import com.leebeebeom.clothinghelper.data.repository.util.NetworkChecker
import com.leebeebeom.clothinghelper.data.repository.util.WifiException
import com.leebeebeom.clothinghelper.domain.model.BaseDatabaseContainerModel
import com.leebeebeom.clothinghelper.domain.repository.BaseContainerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn


@Suppress("UNCHECKED_CAST")
abstract class BaseContainerRepositoryImpl<T : BaseDatabaseContainerModel>(
    sortFlow: Flow<SortPreferences>,
    refPath: String,
    appScope: CoroutineScope,
    networkChecker: NetworkChecker,
) : BaseContainerRepository<T>, BaseDataRepositoryImpl<T>(
    refPath = refPath,
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

    /**
     * @throws FirebaseNetworkException 인터넷 미 연결 시
     * @throws WifiException 사용자가 와이파이로만 연결 선택 시 와이파이 미 연결됐을 경우
     */
    override suspend fun add(data: T, uid: String, onFail: (Exception) -> Unit) {
        val dataWithCreateDate = data.addCreateData()
        val dataWithEditDate = dataWithCreateDate.addEditDate()
        val dataWithKey = dataWithEditDate.addKey(key = getKey(uid = uid)) as T

        super.add(data = dataWithKey, uid = uid, onFail = onFail)
    }

    /**
     * @throws FirebaseNetworkException 인터넷 미 연결 시
     * @throws WifiException 사용자가 와이파이로만 연결 선택 시 와이파이 미 연결됐을 경우
     * @throws NoSuchElementException 본래 데이터를 찾지 못했을 경우
     * @throws IllegalArgumentException 본래 데이터를 삭제하지 못했을 경우
     */
    override suspend fun edit(newData: T, uid: String, onFail: (Exception) -> Unit) {
        val value = allData.value
        val oldData = value.first { it.key == newData.key }

        val newDataWithCreateDate = newData.addCreateData(oldData.createDate)
        val newDataWithEditDate = newDataWithCreateDate.addEditDate() as T

        super.edit(newData = newDataWithEditDate, uid = uid, onFail = onFail)
    }
}