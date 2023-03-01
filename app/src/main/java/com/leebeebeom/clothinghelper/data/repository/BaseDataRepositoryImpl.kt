package com.leebeebeom.clothinghelper.data.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.data.repository.util.*
import com.leebeebeom.clothinghelper.domain.model.BaseDatabaseModel
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

fun DatabaseReference.getContainerRef(uid: String, path: String) = child(uid).child(path)
fun getDbRoot() = Firebase.database.reference

abstract class BaseDataRepositoryImpl<T : BaseDatabaseModel>(
    private val refPath: String,
    private val networkChecker: NetworkChecker,
) : BaseDataRepository<T>, LoadingStateProviderImpl(true) {
    private val dbRoot = getDbRoot()
    override val allData = MutableStateFlow(mutableListOf<T>())

    // TODO 미로그인 시 데이터 사용 Any로 변경
    // TODO 로그인 시 최초 로드 후 원래 데이터 사용 설정으로 변경

    override suspend fun load(
        uid: String?,
        type: Class<T>,
        onFail: (Exception) -> Unit,
    ) = withContext(
        callSite = DatabaseCallSite(callSite = "$type: update"), onFail = onFail
    ) {
        uid?.let {// 로그인 시
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
        networkChecker.checkNetWork()

        val value = allData.value
        value.add(data)
        allData.value = value

        push(uid = uid, t = data)
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun edit(
        newData: T,
        uid: String,
        onFail: (Exception) -> Unit,
    ) = withContext(
        DatabaseCallSite(callSite = "${newData::javaClass}: edit"), loading = false, onFail = onFail
    ) {
        networkChecker.checkNetWork()

        val value = allData.value
        val oldData = value.first { it.key == newData.key }

        val removeResult = value.remove(oldData)

        if (!removeResult) {
            onFail(Exception("edit: 본래 파일 삭제 실패"))
            if (!value.any { it.key == oldData.key }) value.add(oldData)
            return@withContext
        }

        value.add(newData)
        allData.value = value

        push(uid = uid, t = newData)
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

    protected fun getKey(uid: String) = dbRoot.getContainerRef(uid, refPath).push().key!!

    private suspend fun push(
        uid: String,
        t: T,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ): Void = withContext(dispatcher) {
        dbRoot.getContainerRef(uid = uid, path = refPath).child(t.key).setValue(t).await()
    }
}