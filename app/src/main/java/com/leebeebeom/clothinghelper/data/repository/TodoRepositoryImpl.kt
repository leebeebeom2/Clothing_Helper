package com.leebeebeom.clothinghelper.data.repository

import android.content.Context
import com.leebeebeom.clothinghelper.data.datasourse.todo.TodoFirebaseDataSource
import com.leebeebeom.clothinghelper.data.datasourse.todo.TodoRoomDataSource
import com.leebeebeom.clothinghelper.data.repository.container.BaseDataRepositoryImpl
import com.leebeebeom.clothinghelper.data.repository.preference.NetworkPreferenceRepository
import com.leebeebeom.clothinghelper.domain.model.data.Todo
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    todoFirebaseDataSource: TodoFirebaseDataSource,
    todoRoomDataSource: TodoRoomDataSource,
    networkPreferenceRepository: NetworkPreferenceRepository,
) : BaseDataRepositoryImpl<Todo>(
    context = context,
    refPath = DatabasePath.TODOS,
    baseFirebaseDataSource = todoFirebaseDataSource,
    baseRoomDataSource = todoRoomDataSource,
    networkPreferences = networkPreferenceRepository
), TodoRepository