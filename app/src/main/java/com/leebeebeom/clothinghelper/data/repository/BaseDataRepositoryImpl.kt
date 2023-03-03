package com.leebeebeom.clothinghelper.data.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.database.*
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
) : BaseDataRepository<T>, LoadingStateProviderImpl(
    initialValue = true,
    appScope = appScope
) {
    private val dbRoot = getDbRoot()

    // TODO 미로그인 시 데이터 사용 Any로 변경
    // TODO 로그인 시 최초 로드 후 원래 데이터 사용 설정으로 변경

    override suspend fun getAllData(
        dispatcher: CoroutineDispatcher,
        uid: String?,
        type: Class<T>,
        onFail: (Exception) -> Unit,
    ): StateFlow<List<T>> {

        fun onFailWithEmptyFlow(exception: Exception): StateFlow<List<T>> {
            onFail(exception)
            return MutableStateFlow(emptyList())
        }

        return withContext(
            dispatcher = dispatcher,
            callSite = DatabaseCallSite(callSite = "BaseDataRepository: load"),
            onFail = ::onFailWithEmptyFlow
        ) {
            uid?.let {  // 로그인 시
                getAllDataFlow(
                    uid = it,
                    type = type,
                    onFail = ::onFailWithEmptyFlow
                )
            } ?: MutableStateFlow(emptyList()) // 로그아웃 시
        }
    }

    /**
     * @throws FirebaseNetworkException 인터넷 미 연결 시
     * @throws WifiException 사용자가 와이파이로만 연결 선택 시 와이파이 미 연결됐을 경우
     */
    @Suppress("UNCHECKED_CAST")
    override suspend fun add(
        dispatcher: CoroutineDispatcher,
        data: T,
        uid: String,
        onFail: (Exception) -> Unit,
    ) = withContext(
        dispatcher = dispatcher,
        callSite = DatabaseCallSite("${data.javaClass}: add"), loading = false, onFail = onFail
    ) {
        networkChecker.checkNetWork()

        val dataWithKey = data.addKey(key = getKey(uid = uid)) as T

        push(uid = uid, t = dataWithKey, dispatcher = dispatcher)
    }

    /**
     * @throws FirebaseNetworkException 인터넷 미 연결 시
     * @throws WifiException 사용자가 와이파이로만 연결 선택 시 와이파이 미 연결됐을 경우
     * @throws NoSuchElementException 본래 데이터를 찾지 못했을 경우
     * @throws IllegalArgumentException 본래 데이터를 삭제하지 못했을 경우
     */
    @Suppress("UNCHECKED_CAST")
    override suspend fun edit(
        dispatcher: CoroutineDispatcher,
        newData: T,
        uid: String,
        onFail: (Exception) -> Unit,
    ) = withContext(
        dispatcher = dispatcher,
        DatabaseCallSite(callSite = "${newData::javaClass}: edit"), loading = false, onFail = onFail
    ) {
        networkChecker.checkNetWork()

        push(uid = uid, t = newData, dispatcher = dispatcher)
    }

    /**
     * @param callSite 예외 발생 시 로그에 찍힐 Site
     * @param loading true 일 경우 호출 시 로딩 On, 작업이 끝난 후 로딩 Off
     */
    private suspend fun <T> withContext(
        dispatcher: CoroutineDispatcher,
        callSite: DatabaseCallSite,
        loading: Boolean = true,
        onFail: (Exception) -> T,
        task: suspend () -> T,
    ) = withContext(context = dispatcher) {
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

    private fun getAllDataFlow(uid: String, type: Class<T>, onFail: (Exception) -> Flow<List<T>>) =
        callbackFlow {
            MutableStateFlow(mutableListOf<T>())

            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    trySend(snapshot.children.map { it.getValue(type) as T })
                }

                override fun onCancelled(error: DatabaseError) {
                    val exception = error.toException()
                    onFail(exception)
                }

            }

            val containerRef = getDbRoot().getContainerRef(uid = uid, path = refPath)

            containerRef.addValueEventListener(valueEventListener)
            awaitClose { containerRef.removeEventListener(valueEventListener) }
        }.stateIn(
            scope = appScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private fun getKey(uid: String) = dbRoot.getContainerRef(uid, refPath).push().key!!

    private suspend fun push(
        uid: String,
        t: T,
        dispatcher: CoroutineDispatcher,
    ) {
        withContext(dispatcher) {
            dbRoot.getContainerRef(uid = uid, path = refPath).child(t.key).setValue(t).await()
        }
    }
}