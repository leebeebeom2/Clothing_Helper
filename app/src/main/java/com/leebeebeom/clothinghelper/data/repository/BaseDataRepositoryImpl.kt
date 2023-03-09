package com.leebeebeom.clothinghelper.data.repository

import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.data.repository.util.*
import com.leebeebeom.clothinghelper.domain.model.BaseModel
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

fun DatabaseReference.getContainerRef(uid: String, path: String) =
    child(uid).child(path).also { keepSynced(true) }

fun getDbRoot() = Firebase.database.reference

@Suppress("UNCHECKED_CAST")
abstract class BaseDataRepositoryImpl<T : BaseModel>(
    private var refPath: String,
    protected val appScope: CoroutineScope,
    protected val type: Class<T>,
    private val dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
) : BaseDataRepository<T>, LoadingStateProviderImpl() {
    private val dbRoot = getDbRoot()
    private var dataCallback: ValueEventListener? = null
    private var ref: DatabaseReference? = null

    // TODO 콜렉트 쪽에서 예외 처리
    // TODO 네트워크 미 연결 시 스낵바나 알림같을 걸 띄우는 게 나을듯
    // TODO 미로그인 시 데이터 사용 Any로 변경
    // TODO 로그인 시 최초 로드 후 원래 데이터 사용 설정으로 변경

    /**
     * 최초 [collect] 시 [loadingOn] -> [UserRepository.user] collect 시작
     *
     * -> [loadingOn] 호출(user가 변경될때마다 호출됨)
     *
     * -> [UserRepository.user]가 null 일 시 (비 로그인)
     *
     * [emptyList]로 초기화, [loadingOff] 호출
     *
     * -> [UserRepository.user]가 null이 아닐 시 (로그인)
     *
     * [ref] 초기화, [ref]에 [dataCallback] 등록 [dataCallback]의 함수 호출
     *
     * -> [ValueEventListener.onDataChange] 호출 시 데이터 초기 화 후 [loadingOff] 호출
     *
     * -> [ValueEventListener.onCancelled] 호출 시 [loadingOff]만 호출
     * TODO [ValueEventListener.onCancelled] 호출 시 원본 데이터가 없다면 데이터 방출 x
     *
     * 모든 collect가 취소되고 다시 collect 시작 시 [callbackFlow] 빌더 호출
     *
     * @throws DatabaseException onCancelled 호출 시 발생
     * @throws NullPointerException [dataCallback], [ref] 중 하나라도 null일 경우
     */
    override val allData = callbackFlow {
        loadingOn()

        dataCallback ?: run {
            dataCallback = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    trySend(snapshot.children.map { it.getValue(type)!! })
                    loadingOff()
                }

                override fun onCancelled(error: DatabaseError) {
                    loadingOff()
                    throw error.toException()
                }

            }
        }

        val collectJob = launch {
            userRepository.user.collect { user ->
                loadingOn()

                user?.let {
                    ref = dbRoot.getContainerRef(uid = it.uid, path = refPath)
                    ref!!.keepSynced(true)
                    ref!!.addValueEventListener(dataCallback!!)
                } ?: run {
                    ref = null
                    trySend(emptyList())
                    loadingOff()
                }
            }
        }

        awaitClose {
            collectJob.cancel()
            ref!!.keepSynced(false)
            ref!!.removeEventListener(dataCallback!!)
        }
    }.shareIn(
        scope = appScope, started = SharingStarted.WhileSubscribed(5000), replay = 1
    )

    @Suppress("UNCHECKED_CAST")
    override suspend fun add(data: T) = withContext {
        val dataWithKey = data.addKey(key = getKey()) as T

        push(data = dataWithKey)
    }

    override suspend fun push(data: T) {
        withContext { ref!!.child(data.key).setValue(data).await() }
    }

    private suspend fun withContext(task: suspend () -> Unit) = withContext(dispatcher) { task() }

    private fun getKey() = ref!!.push().key!!

}