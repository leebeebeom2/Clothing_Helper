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
import com.leebeebeom.clothinghelperdomain.model.data.BaseModel
import com.leebeebeom.clothinghelperdomain.repository.BaseDataRepository
import com.leebeebeom.clothinghelperdomain.repository.LoadingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

private val db = Firebase.database.apply { setPersistenceEnabled(true) }
private val loadingRepositoryImpl = LoadingRepositoryImpl(true)

abstract class BaseDataRepositoryImpl<T : BaseModel>(
    private val sortFlow: Flow<SortPreferences>,
) : LoadingRepository by loadingRepositoryImpl, BaseDataRepository<T> {
    protected val root = db.reference

    private val _allData = MutableStateFlow(emptyList<T>())
    override val allData = getSortedContainers()
    private fun getSortedContainers() = combine(_allData, sortFlow, transform = ::getSorted)

    protected abstract val refPath: String

    override suspend fun load(uid: String?, type: Class<T>) = databaseTryWithLoading("update") {
        uid?.let {
            val temp = mutableListOf<T>()

            root.child(uid).child(refPath).get().await().children.forEach {
                temp.add((it.getValue(type))!!)
            }

            _allData.update { temp }
            FirebaseResult.Success
        } ?: let {
            _allData.update { emptyList() }
            FirebaseResult.Success
        }
    }

    override suspend fun add(t: T, uid: String) = databaseTryWithTimeOut(1000, "add") {
        val containerRef = root.getContainerRef(uid, refPath)

        val newContainer = getNewContainer(
            t = t, key = getKey(containerRef), createDate = System.currentTimeMillis()
        )

        push(containerRef, newContainer).await()
        _allData.updateMutable { it.add(newContainer) }
        FirebaseResult.Success
    }

    override suspend fun edit(newT: T, uid: String): FirebaseResult =
        databaseTryWithTimeOut(1000, "edit") {
            val newContainerWithNewEditDate =
                getContainerWithNewEditDate(newT = newT, editDate = System.currentTimeMillis())
            root.getContainerRef(uid, refPath).child(newContainerWithNewEditDate.key)
                .setValue(newContainerWithNewEditDate).await()

            _allData.updateMutable {
                val oldContainer =
                    it.first { value -> value.key == newContainerWithNewEditDate.key }
                it.remove(oldContainer)
                it.add(newContainerWithNewEditDate)
            }
            FirebaseResult.Success
        }

    private suspend fun databaseTryWithTimeOut(
        time: Long, site: String, task: suspend () -> FirebaseResult
    ) = withContext(Dispatchers.IO) {
        try {
            withContext(NonCancellable) { withTimeout(time) { task() } }
        } catch (e: Exception) {
            logE(site, e)
            FirebaseResult.Fail(e)
        }
    }

    private suspend fun databaseTryWithLoading(
        site: String, task: suspend () -> FirebaseResult
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

    protected suspend fun push(containerRef: DatabaseReference, t: T) =
        withContext(Dispatchers.IO) { containerRef.child(t.key).setValue(t) }

    protected fun getKey(containerRef: DatabaseReference) = containerRef.push().key!!

    /**
     * 추가될 객체
     *
     * [createDate]를 createDate와 editDate에 할당하여 반환 바람
     */
    protected abstract fun getNewContainer(t: T, key: String, createDate: Long): T
    protected fun DatabaseReference.getContainerRef(uid: String, path: String) =
        child(uid).child(path)

    /**
     * 수정될 객체
     * [editDate]를 editDate에 할당하여 반환 바람
     */
    protected abstract fun getContainerWithNewEditDate(newT: T, editDate: Long): T
}