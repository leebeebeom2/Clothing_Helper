package com.leebeebeom.clothinghelper.data.repository

import com.leebeebeom.clothinghelper.di.AppScope
import com.leebeebeom.clothinghelper.di.DispatcherIO
import com.leebeebeom.clothinghelper.domain.model.Todo
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
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
    refPath = DatabasePath.TODOS,
    appScope = appScope,
    type = Todo::class.java,
    dispatcher = dispatcher,
    userRepository = userRepository
), TodoRepository {
    override val allDataStream =
        super.allDataStream.mapLatest { it.sortedBy { todos -> todos.order } }.stateIn(
            scope = appScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}