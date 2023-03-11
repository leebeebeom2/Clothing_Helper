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
    private var lastCachedData = emptyList<T>()

    // TODO 콜렉트 쪽에서 예외 처리
    // TODO 네트워크 미 연결 시 스낵바나 알림같을 걸 띄우는 게 나을듯
    // TODO 미로그인 시 데이터 사용 Any로 변경
    // TODO 로그인 시 최초 로드 후 원래 데이터 사용 설정으로 변경

    /**
     * @throws DatabaseException onCancelled 호출 시 발생
     * @throws NullPointerException [dataCallback], [ref] 중 하나라도 null일 경우
     */
    override val allData = callbackFlow<DataResult<T>> {
        loadingOn()

        if (dataCallback == null) dataCallback = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                runCatching {
                    lastCachedData = snapshot.children.map { it.getValue(type)!! }
                    trySend(element = DataResult.Success(lastCachedData))
                }.onFailure {
                    trySend(
                        element = DataResult.Fail(
                            lastCachedData = lastCachedData, throwable = it
                        )
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(
                    DataResult.Fail(
                        lastCachedData = lastCachedData, throwable = error.toException()
                    )
                )
            }
        }

        val collectJob = launch {
            userRepository.user.collect { result ->
                loadingOn()

                result.getOrNull()?.let { user ->
                    if (ref != null) {
                        ref!!.keepSynced(false)
                        ref!!.removeEventListener(dataCallback!!)
                        ref = null
                    }
                    ref = dbRoot.getContainerRef(uid = user.uid, path = refPath)
                    ref!!.keepSynced(true)
                    ref!!.addValueEventListener(dataCallback!!)
                } ?: run {
                    if (ref != null) {
                        ref!!.keepSynced(false)
                        ref!!.removeEventListener(dataCallback!!)
                        ref = null
                    }
                    trySend(DataResult.Success(emptyList()))
                }
            }
        }

        awaitClose {
            launch { loadingOff() }
            collectJob.cancel()
            ref?.keepSynced(false)
            ref?.removeEventListener(dataCallback!!)
            ref = null
            dataCallback = null
        }
    }.onEach {
        loadingOff()
    }.flowOn(dispatcher).shareIn(
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

sealed class DataResult<T> {
    data class Success<T>(val allData: List<T>) : DataResult<T>()
    data class Fail<T>(val lastCachedData: List<T>, val throwable: Throwable) : DataResult<T>()
}
