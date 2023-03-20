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
) : BaseDataRepository<T>, LoadingStreamProviderImpl(initialState = true) {
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
    @OptIn(ExperimentalCoroutinesApi::class)
    override val allDataStream =
        userRepository.userStream.flatMapLatest { user ->
            loadingOn()

            user?.let { nonNullUser ->
                callbackFlow {
                    val dataCallback = object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            lastCachedData = snapshot.children.map { it.getValue(type)!! }
                            trySend(element = lastCachedData)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            trySend(lastCachedData)
                            throw error.toException() // TODO 처리
                        }
                    }

                    ref.init(dataCallback = dataCallback)

                    ref = dbRoot.getContainerRef(uid = nonNullUser.uid, path = refPath)
                        .apply {
                            keepSynced(true)
                            addValueEventListener(dataCallback)
                        }

                    awaitClose {
                        loadingOff()
                        ref.init(dataCallback = dataCallback)
                        ref = null
                    }
                }
            } ?: flow { emit(emptyList<T>()) }
        }.onEach { loadingOff() }
            .flowOn(dispatcher)
            .stateIn(
                scope = appScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    @Suppress("UNCHECKED_CAST")
    override suspend fun add(data: T): Job {
        val dataWithKey = data.addKey(key = getKey()) as T

        return push(data = dataWithKey)
    }

    override suspend fun push(data: T) =
        withContext(dispatcher) { launch { ref!!.child(data.key).setValue(data) } }

    protected fun getKey() = ref!!.push().key!!

    private fun DatabaseReference?.init(dataCallback: ValueEventListener) {
        this?.keepSynced(false)
        this?.removeEventListener(dataCallback)
    }
}