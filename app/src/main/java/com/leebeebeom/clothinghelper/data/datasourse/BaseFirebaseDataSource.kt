package com.leebeebeom.clothinghelper.data.datasourse

import com.leebeebeom.clothinghelper.data.repository.firebaseDbRoot
import com.leebeebeom.clothinghelper.data.repository.getContainerRef
import com.leebeebeom.clothinghelper.domain.model.data.BaseModel
import kotlinx.coroutines.tasks.await

abstract class BaseFirebaseDataSource<T : BaseModel> {
    suspend fun getAll(
        uid: String,
        refPath: String,
        type: Class<T>,
    ): List<T> {
        val temp = mutableListOf<T>()

        firebaseDbRoot.getContainerRef(uid = uid, path = refPath).get()
            .await().children.forEach { temp.add((it.getValue(type))!!) }
        return temp
    }
}