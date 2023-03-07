package com.leebeebeom.clothinghelper.data.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.database.*
import com.leebeebeom.clothinghelper.data.repository.BaseDataRepositoryImpl.DataCallback
import com.leebeebeom.clothinghelper.data.repository.util.*
import com.leebeebeom.clothinghelper.domain.model.BaseModel
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

fun DatabaseReference.getContainerRef(uid: String, path: String) = child(uid).child(path)
fun getDbRoot() = FirebaseDatabase.getInstance().reference

@Suppress("UNCHECKED_CAST")
abstract class BaseDataRepositoryImpl<T : BaseModel>(
    private val refPath: String,
    private val networkChecker: NetworkChecker,
    protected val appScope: CoroutineScope,
    protected val type: Class<T>,
    private val dispatcher: CoroutineDispatcher,
) : BaseDataRepository<T>, LoadingStateProviderImpl(
    initialValue = true, appScope = appScope
) {
    private val dbRoot = getDbRoot()
    private var dataCallback: DataCallback<T>? = null

    protected var uid: String? = null

    override val allData = callbackFlow<List<T>> {
        val callback = DataCallback { trySend(it) }

        this@BaseDataRepositoryImpl.dataCallback = callback
        awaitClose { dataCallback = null }
    }.stateIn(
        scope = appScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // TODO 미로그인 시 데이터 사용 Any로 변경
    // TODO 로그인 시 최초 로드 후 원래 데이터 사용 설정으로 변경

    override suspend fun load(
        uid: String?,
        onFail: (Exception) -> Unit,
    ) = withContext(
        callSite = DatabaseCallSite("${type.javaClass}: load"),
        onFail = {
            callbackFlowEmitWrapper { dataCallback -> dataCallback(data = emptyList()) }
            onFail(it)
        }
    ) {
        callbackFlowEmitWrapper { callback ->
            uid?.let {
                val allData = dbRoot.getContainerRef(uid = it, path = refPath).get()
                    .await().children.map { data -> data.getValue(type)!! }
                callback(data = allData)
            } ?: callback(data = emptyList())
        }
    }

    /**
     * @throws FirebaseNetworkException 인터넷 미 연결 시
     * @throws WifiException 사용자가 와이파이로만 연결 선택 시 와이파이 미 연결됐을 경우
     */
    @Suppress("UNCHECKED_CAST")
    override suspend fun add(
        data: T,
        uid: String,
        onFail: (Exception) -> Unit,
    ) = withContext(
        callSite = DatabaseCallSite("${data.javaClass}: add"),
        loading = false,
        onFail = onFail
    ) {
        networkChecker.checkNetWork()

        val dataWithKey = data.addKey(key = getKey(uid = uid)) as T

        push(uid = uid, t = dataWithKey)

        val allData = allData.value.toMutableList()
        allData.add(dataWithKey)
        callbackFlowEmitWrapper { it(allData) }
    }

    /**
     * @throws FirebaseNetworkException 인터넷 미 연결 시
     * @throws WifiException 사용자가 와이파이로만 연결 선택 시 와이파이 미 연결됐을 경우
     * @throws NoSuchElementException 본래 데이터를 찾지 못했을 경우
     * @throws IllegalArgumentException 본래 데이터를 삭제하지 못했을 경우
     */
    @Suppress("UNCHECKED_CAST")
    override suspend fun edit(
        oldData: T,
        newData: T,
        uid: String,
        onFail: (Exception) -> Unit,
    ) = withContext(
        DatabaseCallSite(callSite = "${newData::javaClass}: edit"),
        loading = false,
        onFail = onFail
    ) {
        networkChecker.checkNetWork()

        push(uid = uid, t = newData)

        val allData = allData.value.toMutableList()
        allData.remove(element = oldData)
        allData.add(element = newData)
        callbackFlowEmitWrapper { it(allData) }
    }

    /**
     * @param callSite 예외 발생 시 로그에 찍힐 Site
     * @param loading true 일 경우 호출 시 로딩 On, 작업이 끝난 후 로딩 Off
     */
    private suspend fun withContext(
        callSite: DatabaseCallSite,
        loading: Boolean = true,
        onFail: suspend (Exception) -> Unit,
        task: suspend () -> Unit,
    ) = withContext(dispatcher) {
        try {
            if (loading) loadingOn()
            task()
        } catch (_: CancellationException) {
        } catch (e: Exception) {
            logE(site = callSite.site, e = e)
            onFail(e)
        } finally {
            if (loading) loadingOff()
        }
    }

    private fun getKey(uid: String) = dbRoot.getContainerRef(uid, refPath).push().key!!

    private suspend fun push(uid: String, t: T) {
        withContext(dispatcher) {
            dbRoot.getContainerRef(uid = uid, path = refPath).child(t.key).setValue(t).await()
        }
    }

    private fun interface DataCallback<U : BaseModel> {
        operator fun invoke(data: List<U>)
    }

    private suspend fun callbackFlowEmitWrapper(
        emit: suspend (DataCallback<T>) -> Unit,
    ) = callbackFlowEmit(
        callback = { dataCallback },
        flow = allData,
        emit = emit
    )
}