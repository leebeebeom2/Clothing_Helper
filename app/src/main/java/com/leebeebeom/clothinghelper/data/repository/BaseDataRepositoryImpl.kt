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
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*

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
) : BaseDataRepository<T>, LoadingStateProviderImpl(initialState = true) {
    private val dbRoot = getDbRoot()
    private var ref: DatabaseReference? = null
    private var lastCachedData = emptyList<T>()

    // TODO 콜렉트 쪽에서 예외 처리
    // TODO 네트워크 미 연결 시 스낵바나 알림같을 걸 띄우는 게 나을듯
    // TODO 미로그인 시 데이터 사용 Any로 변경
    // TODO 로그인 시 최초 로드 후 원래 데이터 사용 설정으로 변경

    /**
     * @throws DatabaseException onCancelled 호출 시 발생
     */
    override val allData = callbackFlow<List<T>> {
        loadingOn()

        val dataCallback = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lastCachedData = snapshot.children.map { it.getValue(type)!! }

                trySendBlocking(element = lastCachedData)
                    .onFailure {
                        it?.let { throw it } ?: throw Exception("dataCallback trySend fail")
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }
        }

        val collectJob = launch {
            userRepository.user.catch {
                send(lastCachedData)
                throw it
            }.collect { user ->
                loadingOn()

                user?.let { nonNullUser ->
                    // init
                    ref.init(dataCallback = dataCallback)

                    ref = dbRoot.getContainerRef(uid = nonNullUser.uid, path = refPath)
                        .apply {
                            keepSynced(true)
                            addValueEventListener(dataCallback)
                        }
                } ?: run {
                    ref.init(dataCallback = dataCallback)
                    ref = null
                    send(element = emptyList())
                }
            }
        }

        awaitClose {
            launch { loadingOff() }
            collectJob.cancel()
            ref.init(dataCallback = dataCallback)
            ref = null
        }
    }.onEach { loadingOff() }.onEmpty { emptyList<List<T>>() }.flowOn(dispatcher)

    @Suppress("UNCHECKED_CAST")
    override suspend fun add(data: T) {
        val dataWithKey = data.addKey(key = getKey()) as T

        push(data = dataWithKey)
    }

    override suspend fun push(data: T) {
        withContext(dispatcher) { ref!!.child(data.key).setValue(data) }
    }

    protected fun getKey() = ref!!.push().key!!

    private fun DatabaseReference?.init(dataCallback: ValueEventListener) {
        this?.keepSynced(false)
        this?.removeEventListener(dataCallback)
    }
}