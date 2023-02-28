package com.leebeebeom.clothinghelper.data.repository

import android.content.Context
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.data.repository.preference.NetworkPreferences.WIFI
import com.leebeebeom.clothinghelper.data.repository.util.*
import com.leebeebeom.clothinghelper.domain.model.BaseModel
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.NetworkPreferenceRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

fun DatabaseReference.getContainerRef(uid: String, path: String) = child(uid).child(path)

abstract class BaseDataRepositoryImpl<T : BaseModel>(
    private val context: Context,
    private val refPath: String,
    private val networkChecker: NetworkChecker,
    private val networkPreferences: NetworkPreferenceRepository,
    private val appCoroutineScope: CoroutineScope,
) : BaseDataRepository<T>, LoadingStateProviderImpl(true) {
    private val dbRoot = Firebase.database.reference
    override val allData = MutableStateFlow(mutableListOf<T>())

    // TODO 미로그인 시 데이터 사용 Any로 변경
    // TODO 로그인 시 최초 로드 후 원래 데이터 사용 설정으로 변경
    // TODO collect catch 추가
    // TODO collect catch-emit 추가
    // TODO WIFI or ANY 분기

    override suspend fun load(
        uid: String?,
        type: Class<T>,
        onFail: (Exception) -> Unit,
    ) = withContext(
        callSite = DatabaseCallSite(callSite = "$type: update"), onFail = onFail
    ) {
        uid?.let {// 로그인 시
            checkNetWork()

            val temp = mutableListOf<T>()
            dbRoot.getContainerRef(it, refPath).get()
                .await().children.forEach { snapshot -> temp.add(snapshot.getValue(type)!!) }

            allData.value = temp
        } ?: run { allData.value = mutableListOf() } // 로그아웃 시
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun add(data: T, uid: String, onFail: (Exception) -> Unit) = withContext(
        callSite = DatabaseCallSite("${data.javaClass}: add"), loading = false, onFail = onFail
    ) {
        checkNetWork()

        val dataWithKey = data.addKey(key = getKey(uid = uid)) as T

        val value = allData.value
        val result = value.add(dataWithKey)
        if (!result) {
            onFail(Exception("add 실패"))
            return@withContext
        }
        allData.value = value

        push(uid = uid, t = dataWithKey) // TODO Wokrker
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun edit(
        newData: T,
        uid: String,
        onFail: (Exception) -> Unit,
    ) = withContext(
        DatabaseCallSite(callSite = "${newData::javaClass}: edit"), loading = false, onFail = onFail
    ) {
        checkNetWork()

        val value = allData.value
        val oldData = value.find { it.key == newData.key }
        if (oldData == null) {
            onFail(NullPointerException("edit: 본래 파일 찾기 실패"))
            return@withContext
        }

        val result = value.remove(oldData)
        if (!result) {
            onFail(Exception("edit 실패"))
            if (!value.any { it.key == oldData.key }) {
                value.add(oldData)
            }
            return@withContext
        }
        allData.value = value

        push(uid = uid, t = newData) //TODO workder
    }

    /**
     * @param callSite 예외 발생 시 로그에 찍힐 Site
     * @param loading true 일 경우 호출 시 로딩 On, 작업이 끝난 후 로딩 Off
     */
    private suspend fun withContext(
        callSite: DatabaseCallSite,
        loading: Boolean = true,
        onFail: (Exception) -> Unit,
        task: suspend () -> Unit,
    ) = withContext(Dispatchers.IO) {
        try {
            if (loading) loadingOn()
            task()
        } catch (e: Exception) {
            logE(site = callSite.site, e = e)
            onFail(e)
        } finally {
            if (loading) loadingOff()
        }
    }

    private fun getKey(uid: String) = dbRoot.getContainerRef(uid, refPath).push().key!!

    private suspend fun checkNetWork() {
        if (!networkChecker.networkCheck()) throw FirebaseNetworkException("인터넷 미연결")
        // 와이파이 선택 시 와이파이에 연결되지 않은 경우
        if (networkPreferences.network.first() == WIFI && !networkChecker.isWifiConnected()) throw WifiException
    }

    private suspend fun push(
        uid: String,
        t: T,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ): Void = withContext(dispatcher) {
        dbRoot.getContainerRef(uid = uid, path = refPath).child(t.key).setValue(t).await()
    }
}

object WifiException : Exception()