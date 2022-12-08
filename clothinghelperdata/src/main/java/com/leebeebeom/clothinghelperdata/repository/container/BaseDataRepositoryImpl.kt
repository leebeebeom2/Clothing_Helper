package com.leebeebeom.clothinghelperdata.repository.container

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelperdata.repository.base.LoadingRepositoryImpl
import com.leebeebeom.clothinghelperdata.repository.util.logE
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.model.Order
import com.leebeebeom.clothinghelperdomain.model.Sort
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

class BaseDataRepositoryImpl<T : BaseModel>(
    sortFlow: Flow<SortPreferences>, private val refPath: String
) : LoadingRepository by loadingRepositoryImpl, BaseDataRepository<T> {
    val root = db.reference

    private val _allData = MutableStateFlow(emptyList<T>())
    override val allData = combine(_allData, sortFlow, transform = ::getSortedData)

    override suspend fun load(uid: String?, type: Class<T>) = databaseTryWithLoading("update") {
        uid?.let {
            val temp = mutableListOf<T>()

            root.getContainerRef(uid, refPath).get().await().children.forEach {
                temp.add((it.getValue(type))!!)
            }

            _allData.update { temp }
            FirebaseResult.Success
        } ?: let {
            _allData.update { emptyList() }
            FirebaseResult.Success
        }
    }

    override suspend fun add(t: T, uid: String) =
        databaseTryWithTimeOut(1000, "add") {
            val containerRef = root.getContainerRef(uid, refPath)

            @Suppress("UNCHECKED_CAST")
            val tWithKey = t.addKey(getKey(containerRef)) as T

            push(containerRef, tWithKey).await()
            _allData.updateList { it.add(tWithKey) }
            FirebaseResult.Success
        }

    override suspend fun edit(newT: T, uid: String): FirebaseResult =
        databaseTryWithTimeOut(1000, "edit") {
            root.getContainerRef(uid, refPath).child(newT.key).setValue(newT).await()

            _allData.updateList {
                val oldT = it.first { oldT -> oldT.key == newT.key }
                it.remove(oldT)
                it.add(newT)
            }
            FirebaseResult.Success
        }

    private suspend fun databaseTryWithTimeOut(
        time: Long, callSite: String, task: suspend () -> FirebaseResult
    ) = withContext(Dispatchers.IO) {
        try {
            withContext(NonCancellable) { withTimeout(time) { task() } }
        } catch (e: Exception) {
            logE(callSite, e)
            FirebaseResult.Fail(e)
        }
    }

    private suspend fun databaseTryWithLoading(
        callSite: String, task: suspend () -> FirebaseResult
    ) = withContext(Dispatchers.IO) {
        try {
            loadingRepositoryImpl.loadingOn()
            withContext(NonCancellable) { task() }
        } catch (e: Exception) {
            logE(callSite, e)
            FirebaseResult.Fail(e)
        } finally {
            loadingRepositoryImpl.loadingOff()
        }
    }

    fun getKey(containerRef: DatabaseReference) = containerRef.push().key!!

    suspend fun push(containerRef: DatabaseReference, t: T) =
        withContext(Dispatchers.IO) { containerRef.child(t.key).setValue(t) }

    private fun <T : BaseModel> getSortedData(
        allData: List<T>, sortPreferences: SortPreferences
    ): List<T> {
        val sort = sortPreferences.sort
        val order = sortPreferences.order

        return when {
            sort == Sort.NAME && order == Order.ASCENDING -> allData.sortedBy { it.name }
            sort == Sort.NAME && order == Order.DESCENDING -> allData.sortedByDescending { it.name }
            sort == Sort.CREATE && order == Order.ASCENDING -> allData.sortedBy { it.createDate }
            sort == Sort.CREATE && order == Order.DESCENDING -> allData.sortedByDescending { it.createDate }
            sort == Sort.EDIT && order == Order.ASCENDING -> allData.sortedBy { it.editDate }
            sort == Sort.EDIT && order == Order.DESCENDING -> allData.sortedByDescending { it.editDate }
            else -> allData
        }
    }

    private fun <T> MutableStateFlow<List<T>>.updateList(task: (MutableList<T>) -> Unit) {
        val temp = value.toMutableList()
        task(temp)
        update { temp }
    }
}

fun DatabaseReference.getContainerRef(uid: String, path: String) =
    child(uid).child(path)