package com.leebeebeom.clothinghelper.data.repository

import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.data.repository.util.*
import com.leebeebeom.clothinghelper.domain.model.BaseModel
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import com.leebeebeom.clothinghelper.domain.repository.DataResult
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

fun DatabaseReference.getContainerRef(uid: String, path: String) =
    child(uid).child(path).also { keepSynced(true) }

fun getDbRoot() = Firebase.database.reference

@Suppress("UNCHECKED_CAST")
abstract class BaseDataRepositoryImpl<T : BaseModel>(
    private var refPath: DataBasePath,
    protected val appScope: CoroutineScope,
    protected val type: Class<T>,
    private val dispatcher: CoroutineDispatcher,
    userRepository: UserRepository,
) : BaseDataRepository<T> {
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
    override val allDataFlow = userRepository.userFlow.flatMapLatest { user ->
        user?.let { nonNullUser ->
            callbackFlow {
                val dataCallback = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        lastCachedData = snapshot.children.map { it.getValue(type)!! }
                        trySend(element = DataResult.Success(lastCachedData))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        trySend(element = DataResult.Fail(lastCachedData, error.toException()))
                    }
                }

                ref.init(dataCallback = dataCallback)

                ref = dbRoot.getContainerRef(uid = nonNullUser.uid, path = refPath.path)
                    .apply {
                        keepSynced(true)
                        addValueEventListener(dataCallback)
                    }

                awaitClose {
                    ref.init(dataCallback = dataCallback)
                    ref = null
                }
            }
        } ?: flow { emit(DataResult.Success(data = emptyList<T>())) }
    }.shareIn(
        scope = appScope,
        started = SharingStarted.WhileSubscribed(5000),
        replay = 1
    )

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