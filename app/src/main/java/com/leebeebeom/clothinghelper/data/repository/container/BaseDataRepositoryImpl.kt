package com.leebeebeom.clothinghelper.data.repository.container

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.data.repository.util.DatabaseCallSite
import com.leebeebeom.clothinghelper.data.repository.util.LoadingStateProviderImpl
import com.leebeebeom.clothinghelper.data.repository.util.logE
import com.leebeebeom.clothinghelper.domain.model.Order.ASCENDING
import com.leebeebeom.clothinghelper.domain.model.Order.DESCENDING
import com.leebeebeom.clothinghelper.domain.model.Sort.*
import com.leebeebeom.clothinghelper.domain.model.SortPreferences
import com.leebeebeom.clothinghelper.domain.model.data.BaseModel
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

/**
 * setPersistenceEnabled는 네트워크 미 연결 시에도 데이터를 조회할 수 있게 해줌
 */
val dbRoot = Firebase.database.apply { setPersistenceEnabled(true) }.reference

fun DatabaseReference.getContainerRef(uid: String, path: String) = child(uid).child(path)

abstract class BaseDataRepositoryImpl<T : BaseModel>(
    sortFlow: Flow<SortPreferences>,
    private val refPath: String,
) : BaseDataRepository<T>, LoadingStateProviderImpl(true) {
    private val _allData = MutableStateFlow(emptyList<T>())

    /**
     * _allData 혹은 sortFlow가 변경되면 업데이트 됨
     */
    override val allData = combine(flow = _allData, flow2 = sortFlow, transform = ::getSortedData)


    /**
     * @param uid [uid]가 null 일 경우 [_allData]는 [emptyList]로 업데이트 됨
     */
    override suspend fun load(uid: String?, type: Class<T>, onFail: (Exception) -> Unit) =
        databaseTryWithLoading(
            callSite = DatabaseCallSite(callSite = "$type: update"), onFail = onFail
        ) {
            uid?.let {
                val temp = mutableListOf<T>()

                dbRoot.getContainerRef(uid = uid, path = refPath).get()
                    .await().children.forEach { temp.add((it.getValue(type))!!) }

                _allData.update { temp }
            } ?: let { _allData.update { emptyList() } }
        }

    /**
     * 응답이 3를 초과할 경우 네트워크 미 연결로 간주, [TimeoutCancellationException] 발생
     */
    override suspend fun add(data: T, uid: String, onFail: (Exception) -> Unit) =
        databaseTryWithTimeOut(
            3000, DatabaseCallSite("${data.javaClass}: add"), onFail = onFail
        ) {
            val containerRef = dbRoot.getContainerRef(uid = uid, path = refPath)

            @Suppress("UNCHECKED_CAST") val dataWithKey =
                data.addKey(key = getKey(containerRef = containerRef)) as T

            push(containerRef = containerRef, t = dataWithKey).await()
            _allData.updateList { it.add(dataWithKey) }
        }

    /**
     * 응답이 3를 초과할 경우 네트워크 미 연결로 간주, [TimeoutCancellationException] 발생
     */
    override suspend fun edit(
        newData: T,
        uid: String,
        onFail: (Exception) -> Unit,
    ) = databaseTryWithTimeOut(
        3000,
        DatabaseCallSite(callSite = "${newData::javaClass}: edit"),
        onFail = onFail
    ) {
        dbRoot.getContainerRef(uid = uid, path = refPath).child(newData.key).setValue(newData)
            .await()

        _allData.updateList {
            val oldData = it.first { oldT -> oldT.key == newData.key }
            it.remove(oldData)
            it.add(newData)
        }
    }

    /**
     * 호출 시 로딩 없음
     *
     * 취소할 수 없음
     *
     * 응답 시간이 [time]을 초과할 경우 네트워크 미 연결로 간주하며 [TimeoutCancellationException] 발생
     *
     * @param callSite 예외 발생 시 로그에 찍힐 Site
     */
    private suspend fun databaseTryWithTimeOut(
        time: Long,
        callSite: DatabaseCallSite,
        onFail: (Exception) -> Unit,
        task: suspend () -> Unit,
    ) = withContext(Dispatchers.IO) {
        try {
            withContext(context = NonCancellable) { withTimeout(timeMillis = time) { task() } }
        } catch (e: Exception) {
            logE(site = callSite.site, e = e)
            onFail(e)
        }
    }

    /**
     * 호출 시 로딩 On
     *
     * 작업이 끝날 시 로딩 Off
     *
     * 취소할 수 없음
     *
     * @param callSite 예외 발생 시 로그에 찍힐 Site
     */
    private suspend fun databaseTryWithLoading(
        callSite: DatabaseCallSite,
        onFail: (Exception) -> Unit,
        task: suspend () -> Unit,
    ) = withContext(Dispatchers.IO) {
        try {
            loadingOn()
            withContext(context = NonCancellable) { task() }
        } catch (e: Exception) {
            logE(site = callSite.site, e = e)
            onFail(e)
        } finally {
            loadingOff()
        }
    }

    private fun getKey(containerRef: DatabaseReference) = containerRef.push().key!!

    private suspend fun push(containerRef: DatabaseReference, t: T) =
        withContext(Dispatchers.IO) { containerRef.child(t.key).setValue(t) }

    private fun <T : BaseModel> getSortedData(
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

    private fun <T> MutableStateFlow<List<T>>.updateList(task: (MutableList<T>) -> Unit) {
        val temp = value.toMutableList()
        task(temp)
        update { temp }
    }
}