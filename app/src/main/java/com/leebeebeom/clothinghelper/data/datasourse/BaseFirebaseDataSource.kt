package com.leebeebeom.clothinghelper.data.datasourse

import com.leebeebeom.clothinghelper.data.repository.firebaseDbRoot
import com.leebeebeom.clothinghelper.data.repository.getContainerRef
import com.leebeebeom.clothinghelper.domain.model.data.BaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

abstract class BaseFirebaseDataSource<T : BaseModel>(private val refPath: String) {
    suspend fun getAll(
        uid: String,
        type: Class<T>,
    ): List<T> = withContext(Dispatchers.IO) {
        val temp = mutableListOf<T>()

        firebaseDbRoot.getContainerRef(uid = uid, path = refPath).get()
            .await().children.forEach { temp.add((it.getValue(type))!!) }
        temp
    }

    suspend fun push(uid: String, t: T) {
        firebaseDbRoot.getContainerRef(uid, refPath).child(t.key).setValue(t).await()
    }
}