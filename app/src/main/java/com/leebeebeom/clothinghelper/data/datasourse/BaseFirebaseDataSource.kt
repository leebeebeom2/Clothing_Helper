package com.leebeebeom.clothinghelper.data.datasourse

import com.leebeebeom.clothinghelper.data.repository.firebaseDbRoot
import com.leebeebeom.clothinghelper.data.repository.getContainerRef
import com.leebeebeom.clothinghelper.domain.model.BaseFirebaseModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

abstract class BaseFirebaseDataSource<T : BaseFirebaseModel>(private val refPath: String) {
    suspend fun getAll(
        uid: String,
        type: Class<T>,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ): List<T> = withContext(dispatcher) {
        val temp = mutableListOf<T>()

        firebaseDbRoot.getContainerRef(uid = uid, path = refPath).get()
            .await().children.forEach { temp.add((it.getValue(type))!!) }
        temp
    }

    suspend fun push(uid: String, t: T, dispatcher: CoroutineDispatcher = Dispatchers.IO): Void =
        withContext(dispatcher) {
            firebaseDbRoot.getContainerRef(uid, refPath).child(t.key).setValue(t).await()
        }
}