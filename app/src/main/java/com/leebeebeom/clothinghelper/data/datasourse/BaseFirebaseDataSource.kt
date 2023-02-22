package com.leebeebeom.clothinghelper.data.datasourse

import com.leebeebeom.clothinghelper.data.repository.firebaseDbRoot
import com.leebeebeom.clothinghelper.data.repository.getContainerRef
import com.leebeebeom.clothinghelper.domain.model.data.BaseModel
import kotlinx.coroutines.tasks.await

abstract class BaseFirebaseDataSource<T : BaseModel>(private val refPath: String) {
    suspend fun getAll(
        uid: String,
        type: Class<T>,
    ): List<T> {
        val temp = mutableListOf<T>()

        firebaseDbRoot.getContainerRef(uid = uid, path = refPath).get()
            .await().children.forEach { temp.add((it.getValue(type))!!) }
        return temp
    }

    suspend fun push(uid: String, t: T) {
        firebaseDbRoot.getContainerRef(uid, refPath).child(t.key).setValue(t).await()
    }
}