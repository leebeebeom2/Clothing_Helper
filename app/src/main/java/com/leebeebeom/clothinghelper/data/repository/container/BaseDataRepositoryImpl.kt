package com.leebeebeom.clothinghelper.data.repository.container

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.data.repository.util.DatabaseCallSite
import com.leebeebeom.clothinghelper.data.repository.util.logE
import com.leebeebeom.clothinghelper.domain.model.FirebaseResult
import com.leebeebeom.clothinghelper.domain.model.FirebaseResult.Success
import com.leebeebeom.clothinghelper.domain.model.Order.ASCENDING
import com.leebeebeom.clothinghelper.domain.model.Order.DESCENDING
import com.leebeebeom.clothinghelper.domain.model.Sort.*
import com.leebeebeom.clothinghelper.domain.model.SortPreferences
import com.leebeebeom.clothinghelper.domain.model.data.BaseModel
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import com.leebeebeom.clothinghelper.util.LoadingStateProviderImpl
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

/**
 * setPersistenceEnabled는 네트워크 미 연결 시에도 데이터를 조회할 수 있게 해줌
 */
val root = Firebase.database.apply { setPersistenceEnabled(true) }.reference

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
     * 성공 시 [FirebaseResult.Success] 반환
     *
     * 실패 시 [Exception]이 담긴 [FirebaseResult.Fail] 반환
     *
     * @param uid [uid]가 null 일 경우 [_allData]는 [emptyList]로 업데이트 됨
     */
    override suspend fun load(uid: String?, type: Class<T>) =
        databaseTryWithLoading(callSite = DatabaseCallSite(callSite = "$type: update")) {
            uid?.let {
                val temp = mutableListOf<T>()

                root.getContainerRef(uid = uid, path = refPath).get().await().children.forEach {
                    temp.add((it.getValue(type))!!)
                }

                _allData.update { temp }
                Success
            } ?: let {
                _allData.update { emptyList() }
                Success
            }
        }

    /**
     * 성공 시 [FirebaseResult.Success] 반환
     *
     * 실패 시 [Exception]이 담긴 [FirebaseResult.Fail] 반환
     *
     * 응답이 3를 초과할 경우 네트워크 미 연결로 간주, [TimeoutCancellationException]이 담긴 [FirebaseResult.Fail] 반환
     */
    override suspend fun add(data: T, uid: String): FirebaseResult =
        databaseTryWithTimeOut(3000, DatabaseCallSite("${data.javaClass}: add")) {
            val containerRef = root.getContainerRef(uid = uid, path = refPath)

            @Suppress("UNCHECKED_CAST")
            val dataWithKey = data.addKey(key = getKey(containerRef = containerRef)) as T

            push(containerRef = containerRef, t = dataWithKey).await()
            _allData.updateList { it.add(dataWithKey) }
            Success
        }

    /**
     * 성공 시 [FirebaseResult.Success] 반환
     *
     * 실패 시 [Exception]이 담긴 [FirebaseResult.Fail] 반환
     *
     * 응답이 3를 초과할 경우 네트워크 미 연결로 간주, [TimeoutCancellationException]이 담긴 [FirebaseResult.Fail] 반환
     */
    override suspend fun edit(
        newData: T,
        uid: String,
    ): FirebaseResult =
        databaseTryWithTimeOut(3000, DatabaseCallSite(callSite = "${newData::javaClass}: edit")) {
            root.getContainerRef(uid = uid, path = refPath).child(newData.key).setValue(newData)
                .await()

            _allData.updateList {
                val oldData = it.first { oldT -> oldT.key == newData.key }
                it.remove(oldData)
                it.add(newData)
            }
            Success
        }

    /**
     * 호출 시 로딩 없음
     *
     * 취소할 수 없음
     *
     * 응답 시간이 [time]을 초과할 경우 네트워크 미 연결로 간주하며 [TimeoutCancellationException]이 담긴 [FirebaseResult.Fail] 반환
     *
     * 다른 예외 발생 시 해당 [Exception]이 담긴 [FirebaseResult.Fail] 반환
     *
     * @param callSite 예외 발생 시 로그에 찍힐 Site
     */
    private suspend fun databaseTryWithTimeOut(
        time: Long,
        callSite: DatabaseCallSite,
        task: suspend () -> FirebaseResult,
    ) = withContext(Dispatchers.IO) {
        try {
            withContext(context = NonCancellable) { withTimeout(timeMillis = time) { task() } }
        } catch (e: Exception) {
            logE(site = callSite.site, e = e)
            FirebaseResult.Fail(exception = e)
        }
    }

    /**
     * 호출 시 로딩 On
     *
     * 작업이 끝날 시 로딩 Off
     *
     * 취소할 수 없음
     *
     * 예외 발생 시 [Exception]이 담긴 [FirebaseResult.Fail] 반환
     *
     * @param callSite 예외 발생 시 로그에 찍힐 Site
     */
    private suspend fun databaseTryWithLoading(
        callSite: DatabaseCallSite,
        task: suspend () -> FirebaseResult,
    ) = withContext(Dispatchers.IO) {
        try {
            loadingOn()
            withContext(context = NonCancellable) { task() }
        } catch (e: Exception) {
            logE(site = callSite.site, e = e)
            FirebaseResult.Fail(exception = e)
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

    private fun DatabaseReference.getContainerRef(uid: String, path: String) =
        child(uid).child(path)
}