package com.leebeebeom.clothinghelper.data.repository

import com.leebeebeom.clothinghelper.data.repository.util.NetworkChecker
import com.leebeebeom.clothinghelper.di.AppScope
import com.leebeebeom.clothinghelper.di.DispatcherIO
import com.leebeebeom.clothinghelper.domain.model.Todo
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class TodoRepositoryImpl @Inject constructor(
    networkChecker: NetworkChecker,
    @AppScope appScope: CoroutineScope,
    @DispatcherIO dispatcher: CoroutineDispatcher,
) : BaseDataRepositoryImpl<Todo>(
    refPath = DatabasePath.TODOS,
    networkChecker = networkChecker,
    appScope = appScope,
    type = Todo::class.java,
    dispatcher = dispatcher
), TodoRepository {
    override val allData: StateFlow<List<Todo>> =
        super.allData.mapLatest { it.sortedBy { todoList -> todoList.order } }
            .stateIn(appScope, SharingStarted.WhileSubscribed(5000), emptyList())
}