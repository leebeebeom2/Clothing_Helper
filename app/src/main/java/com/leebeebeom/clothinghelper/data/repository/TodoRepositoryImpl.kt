package com.leebeebeom.clothinghelper.data.repository

import com.leebeebeom.clothinghelper.data.repository.util.NetworkChecker
import com.leebeebeom.clothinghelper.di.AppScope
import com.leebeebeom.clothinghelper.domain.model.Todo
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepositoryImpl @Inject constructor(
    networkChecker: NetworkChecker,
    @AppScope appScope: CoroutineScope,
) : BaseDataRepositoryImpl<Todo>(
    refPath = DatabasePath.TODOS,
    networkChecker = networkChecker,
    appScope = appScope
), TodoRepository