package com.leebeebeom.clothinghelperdata.repository.base

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelperdata.repository.util.logE
import com.leebeebeom.clothinghelperdomain.model.BaseContainer
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

abstract class BaseContainerRepository<T : BaseContainer> : BaseRepository(true) {
    protected val root = Firebase.database.reference

    protected val allContainers = MutableStateFlow(emptyList<T>())
    private var uid: String = ""
    abstract val refPath: String

    protected suspend fun load(uid: String, type: Class<T>): FirebaseResult {
        return databaseTry(3000, "update") {
            this.uid = uid

            val temp = mutableListOf<T>()
            root.child(uid).child(refPath).get().await().children.forEach {
                temp.add((it.getValue(type))!!)
            }

            allContainers.update { temp }
            FirebaseResult.Success
        }
    }

    protected suspend fun add(value: T): FirebaseResult {
        return databaseTryWithoutLoading(100000, "add") {
            val containerRef = root.getContainerRef(uid, refPath)

            val containerWithKey =
                getContainerWithKey(value, getKey(containerRef), System.currentTimeMillis())

            push(containerRef, containerWithKey).await()
            allContainers.updateMutable { it.add(containerWithKey) }
            FirebaseResult.Success
        }
    }

    protected suspend fun edit(newValue: T): FirebaseResult {
        return databaseTryWithoutLoading(1000, "edit") {
            root.getContainerRef(uid, refPath).child(newValue.key).setValue(newValue).await()

            allContainers.updateMutable {
                val oldContainer = it.first { value -> value.key == newValue.key }
                it.remove(oldContainer)
                it.add(newValue)
            }
            FirebaseResult.Success
        }
    }

    protected suspend fun push(
        containerRef: DatabaseReference, value: T
    ): Task<Void> {
        return withContext(Dispatchers.IO) {
            containerRef.child(value.key).setValue(value)
        }
    }

    protected fun getKey(containerRef: DatabaseReference): String {
        return containerRef.push().key!!
    }

    private suspend fun databaseTryWithoutLoading(
        time: Long,
        site: String,
        task: suspend () -> FirebaseResult
    ): FirebaseResult {
        return withContext(Dispatchers.IO) {
            try {
                withTimeout(time) { task() }
            } catch (e: TimeoutCancellationException) {
                logE(site, e)
                FirebaseResult.Fail(e)
            } catch (e: Exception) {
                logE(site, e)
                FirebaseResult.Fail(e)
            }
        }
    }

    private suspend fun databaseTry(
        time: Long,
        site: String,
        task: suspend () -> FirebaseResult
    ): FirebaseResult {
        return withContext(Dispatchers.IO) {
            try {
                loadingOn()
                withTimeout(time) { task() }
            } catch (e: Exception) {
                logE(site, e)
                FirebaseResult.Fail(e)
            } finally {
                loadingOff()
            }
        }
    }

    protected abstract fun getContainerWithKey(value: T, key: String, createDate: Long): T
    protected abstract fun getContainerWithKey(value: T, key: String): T

    protected fun DatabaseReference.getContainerRef(uid: String, path: String): DatabaseReference {
        return child(uid).child(path)
    }
}

object DatabasePath {
    const val SUB_CATEGORIES = "sub categories"
    const val USER_INFO = "user info"
    const val FOLDERS = "folders"
}