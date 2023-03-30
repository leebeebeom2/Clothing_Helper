package com.leebeebeom.clothinghelper.data.repository

import com.leebeebeom.clothinghelper.di.AppScope
import com.leebeebeom.clothinghelper.di.DispatcherIO
import com.leebeebeom.clothinghelper.domain.model.Todo
import com.leebeebeom.clothinghelper.domain.repository.DataResult
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class TodoRepositoryImpl @Inject constructor(
    @AppScope appScope: CoroutineScope,
    @DispatcherIO dispatcher: CoroutineDispatcher,
    userRepository: UserRepository,
) : BaseDataRepositoryImpl<Todo>(
    refPath = DataBasePath.Todo,
    appScope = appScope,
    type = Todo::class.java,
    dispatcher = dispatcher,
    userRepository = userRepository
), TodoRepository {
    override val allDataFlow =
        super.allDataFlow.mapLatest { dataResult ->
            when (dataResult) {
                is DataResult.Success -> DataResult.Success(dataResult.data.sortedBy { it.order }
                    .toImmutableList())
                is DataResult.Fail -> dataResult
            }
        }.shareIn(
            scope = appScope,
            started = SharingStarted.WhileSubscribed(5000),
            replay = 1
        )
}