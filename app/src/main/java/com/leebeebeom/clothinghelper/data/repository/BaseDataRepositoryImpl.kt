package com.leebeebeom.clothinghelper.data.repository

import android.content.Context
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.data.datasourse.BaseFirebaseDataSource
import com.leebeebeom.clothinghelper.data.datasourse.BaseRoomDataSource
import com.leebeebeom.clothinghelper.data.repository.preference.NetworkPreferences
import com.leebeebeom.clothinghelper.data.repository.util.DatabaseCallSite
import com.leebeebeom.clothinghelper.data.repository.util.LoadingStateProviderImpl
import com.leebeebeom.clothinghelper.data.repository.util.logE
import com.leebeebeom.clothinghelper.data.repository.util.networkCheck
import com.leebeebeom.clothinghelper.domain.model.data.BaseModel
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.NetworkPreferenceRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

val firebaseDbRoot = Firebase.database.reference

fun DatabaseReference.getContainerRef(uid: String, path: String) = child(uid).child(path)

abstract class BaseDataRepositoryImpl<T : BaseModel>(
    private val context: Context,
    private val refPath: String,
    private val baseFirebaseDataSource: BaseFirebaseDataSource<T>,
    private val baseRoomDataSource: BaseRoomDataSource<T>,
    private val networkPreferences: NetworkPreferenceRepository,
) : BaseDataRepository<T>, LoadingStateProviderImpl(true) {
    override val allData = baseRoomDataSource.getAll()

    /**
     * 로그인 상태로 앱 실행 시 로컬 데이터를 가져옴
     * 새 로그인 시([uid]가 null이 아닐 경우) 로컬 데이터가 없기 때문에 서버에서 가져옴
     * 로그아웃 시([uid]가 null일 경우) 로컬 데이터 삭제
     *
     * @param uid 로그아웃 상태일 경우 null
     */
    override suspend fun load(uid: String?, type: Class<T>, onFail: (Exception) -> Unit) =
        databaseTry(
            callSite = DatabaseCallSite(callSite = "$type: update"), onFail = onFail
        ) {
            // TODO 로그인 시 최초 로드 후 원래 데이터 사용 설정으로 변경하도록
            uid?.let { // 로그인 상태
                // 로컬 데이터가 존재할 경우
                if (allData.last().isNotEmpty()) return@databaseTry
                // 데이터를 사용하지 않을 경우(로그아웃 된 경우 데이터 사용하도록 되어있음)
                if (isSelectedNetworkLocal()) return@databaseTry
                // 인터넷에 연결되어있지 않은 경우
                if (!networkCheck(context)) throw FirebaseNetworkException("인터넷 미연결")

                val allRemoteData =
                    baseFirebaseDataSource.getAll(uid = uid, refPath = refPath, type = type)
                // 서버에도 데이터가 없을 경우
                if (allRemoteData.isEmpty()) return@databaseTry
                baseRoomDataSource.insert(data = allRemoteData)
            } ?: baseRoomDataSource.deleteAll() // 로그아웃 상태
        }

    @Suppress("UNCHECKED_CAST")
    override suspend fun add(data: T, uid: String, onFail: (Exception) -> Unit) = databaseTry(
        callSite = DatabaseCallSite("${data.javaClass}: add"), loading = false, onFail = onFail
    ) {
        val containerRef = firebaseDbRoot.getContainerRef(uid = uid, path = refPath)

        val dataWithKey = data.addKey(key = getKey(containerRef = containerRef)) as T

        if (isSelectedNetworkLocal() || !networkCheck(context)) {
            baseRoomDataSource.insert(data = dataWithKey)
            return@databaseTry
        }

        val syncedData = dataWithKey.isSynced as T
        push(containerRef = containerRef, t = syncedData).await()
        baseRoomDataSource.insert(data = syncedData)
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun edit(
        newData: T,
        uid: String,
        onFail: (Exception) -> Unit,
    ) = databaseTry(
        DatabaseCallSite(callSite = "${newData::javaClass}: edit"), loading = false, onFail = onFail
    ) {
        if (isSelectedNetworkLocal() || !networkCheck(context)) {
            baseRoomDataSource.update(data = newData)
            return@databaseTry
        }

        val syncedData = newData.isSynced as T
        firebaseDbRoot.getContainerRef(uid = uid, path = refPath)
            .child(newData.key).setValue(newData).await()
        baseRoomDataSource.update(data = syncedData)
    }

    /**
     * 호출 시 로딩 On
     *
     * 작업이 끝날 시 로딩 Off
     *
     * 취소할 수 없음
     *
     * @param callSite 예외 발생 시 로그에 찍힐 Site
     * @param loading true 일 경우 호출 시 로딩 On, 작업이 끝난 후 로딩 Off
     */
    private suspend fun databaseTry(
        callSite: DatabaseCallSite,
        loading: Boolean = true,
        onFail: (Exception) -> Unit,
        task: suspend CoroutineScope.() -> Unit,
    ) = withContext(Dispatchers.IO) {
        try {
            if (loading) loadingOn()
            withContext(context = NonCancellable) { task() }
        } catch (e: Exception) {
            logE(site = callSite.site, e = e)
            onFail(e)
        } finally {
            if (loading) loadingOff()
        }
    }

    // 오프라인에서도 작동함
    private fun getKey(containerRef: DatabaseReference) = containerRef.push().key!!

    private suspend fun push(containerRef: DatabaseReference, t: T) =
        withContext(Dispatchers.IO) { containerRef.child(t.key).setValue(t) }

    private suspend fun isSelectedNetworkLocal() =
        networkPreferences.network.last() == NetworkPreferences.LOCAL
}