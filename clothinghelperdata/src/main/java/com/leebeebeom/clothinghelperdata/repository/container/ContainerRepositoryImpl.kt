package com.leebeebeom.clothinghelperdata.repository.container

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelperdata.repository.base.LoadingRepositoryImpl
import com.leebeebeom.clothinghelperdata.repository.util.getSorted
import com.leebeebeom.clothinghelperdata.repository.util.logE
import com.leebeebeom.clothinghelperdata.repository.util.updateMutable
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.model.SortPreferences
import com.leebeebeom.clothinghelperdomain.model.container.BaseContainer
import com.leebeebeom.clothinghelperdomain.repository.ContainerRepository
import com.leebeebeom.clothinghelperdomain.repository.LoadingRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await

private val db = Firebase.database.apply { setPersistenceEnabled(true) }
private val loadingRepositoryImpl = LoadingRepositoryImpl(true)

abstract class ContainerRepositoryImpl<T : BaseContainer> :
    LoadingRepository by loadingRepositoryImpl, ContainerRepository<T> {
    protected val root = db.reference

    private val allContainers = MutableStateFlow(emptyList<T>())
    protected abstract val refPath: String

    /**
     * [T]리스트의 상태가 변하거나 정렬이 변할 시 정렬된 [T] 리스트 [Flow] 반환
     */
    protected fun getSortedContainers(sortFlow: Flow<SortPreferences>) =
        combine(allContainers, sortFlow) { allContainers, sort -> getSorted(allContainers, sort) }

    /**
     * uid가 null이면 [allContainers]를 빈 리스트로 [update]
     */
    override suspend fun load(uid: String?, type: Class<T>) =
        databaseTryWithLoading("update") {
            uid?.let {
                val temp = mutableListOf<T>()

                root.child(uid).child(refPath).get().await().children.forEach {
                    temp.add((it.getValue(type))!!)
                }

                allContainers.update { temp }
                FirebaseResult.Success
            } ?: let {
                allContainers.update { emptyList() }
                FirebaseResult.Success
            }
        }

    /**
     * 1초간 응답없을 시 네트워크 미 연결로 간주
     *
     * [TimeoutCancellationException]이 포함된 [FirebaseResult.Fail] 반환
     */
    override suspend fun add(t: T, uid: String) =
        databaseTryWithTimeOut(1000, "add") {
            val containerRef = root.getContainerRef(uid, refPath)

            val newContainer = getNewContainer(
                value = t,
                key = getKey(containerRef),
                createDate = System.currentTimeMillis()
            )

            push(containerRef, newContainer).await()
            allContainers.updateMutable { it.add(newContainer) }
            FirebaseResult.Success
        }

    /**
     * 1초간 응답없을 시 네트워크 미 연결로 간주
     *
     * [TimeoutCancellationException]이 포함된 [FirebaseResult.Fail] 반환
     */
    override suspend fun edit(t: T, uid: String): FirebaseResult =
        databaseTryWithTimeOut(1000, "edit") {
            val newContainerWithNewEditDate =
                getContainerWithNewEditDate(t, System.currentTimeMillis())
            root.getContainerRef(uid, refPath).child(newContainerWithNewEditDate.key)
                .setValue(newContainerWithNewEditDate).await()

            allContainers.updateMutable {
                val oldContainer =
                    it.first { value -> value.key == newContainerWithNewEditDate.key }
                it.remove(oldContainer)
                it.add(newContainerWithNewEditDate)
            }
            FirebaseResult.Success
        }

    private suspend fun databaseTryWithTimeOut(
        time: Long,
        site: String,
        task: suspend () -> FirebaseResult
    ) = withContext(Dispatchers.IO) {
        try {
            withContext(NonCancellable) { withTimeout(time) { task() } }
        } catch (e: Exception) {
            logE(site, e)
            FirebaseResult.Fail(e)
        }
    }

    private suspend fun databaseTryWithLoading(
        site: String,
        task: suspend () -> FirebaseResult
    ) = withContext(Dispatchers.IO) {
        try {
            loadingRepositoryImpl.loadingOn()
            withContext(NonCancellable) { task() }
        } catch (e: Exception) {
            logE(site, e)
            FirebaseResult.Fail(e)
        } finally {
            loadingRepositoryImpl.loadingOff()
        }
    }

    protected suspend fun push(containerRef: DatabaseReference, value: T) =
        withContext(Dispatchers.IO) {
            containerRef.child(value.key).setValue(value)
        }

    protected fun getKey(containerRef: DatabaseReference) = containerRef.push().key!!

    /**
     * 추가될 객체
     *
     * [createDate]를 createDate와 editDate에 할당하여 반환 바람
     */
    protected abstract fun getNewContainer(value: T, key: String, createDate: Long): T
    protected fun DatabaseReference.getContainerRef(uid: String, path: String) =
        child(uid).child(path)

    /**
     * 수정될 객체
     * [editDate]를 editDate에 할당하여 반환 바람
     */
    protected abstract fun getContainerWithNewEditDate(value: T, editDate: Long): T
}