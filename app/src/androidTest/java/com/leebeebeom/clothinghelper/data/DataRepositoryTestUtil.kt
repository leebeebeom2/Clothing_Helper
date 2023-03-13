@file:OptIn(ExperimentalCoroutinesApi::class)

package com.leebeebeom.clothinghelper.data

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.leebeebeom.clothinghelper.RepositoryProvider
import com.leebeebeom.clothinghelper.data.repository.DataResult
import com.leebeebeom.clothinghelper.domain.model.BaseModel
import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.*

const val dataLoadingTag = "loading"

@OptIn(ExperimentalCoroutinesApi::class)
class DataRepositoryTestUtil<T : BaseModel, U : BaseDataRepository<T>>(
    repositoryProvider: RepositoryProvider,
    val repository: U,
    val userRepositoryTestUtil: UserRepositoryTestUtil = UserRepositoryTestUtil(repositoryProvider = repositoryProvider),
) {
    val dispatcher = repositoryProvider.dispatcher

    suspend fun allDataCollect(backgroundScope: CoroutineScope) =
        backgroundScope.launch(dispatcher) { repository.allData.collect() }

    suspend fun loadingCollect(backgroundScope: CoroutineScope) =
        backgroundScope.launch(dispatcher) {
            repository.isLoading.distinctUntilChanged().collect {
                Log.d(dataLoadingTag, "loadingCollect: 호출 $it")
            }
        }

    suspend fun add(data: T) = repository.add(data = data)

    suspend fun edit(newData: T) = repository.push(data = newData)

    suspend fun removeAllData() {
        FirebaseDatabase.getInstance().reference.child(userRepositoryTestUtil.getUid()!!)
            .removeValue().await()
    }

    suspend fun getAllData() =
        when (val result = repository.allData.first()) {
            is DataResult.Success -> result.allData
            is DataResult.Fail -> throw result.throwable
        }

    suspend fun getFirstData() = getAllData().first()
}