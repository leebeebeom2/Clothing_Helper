package com.leebeebeom.clothinghelper.data.datasourse.todo

import com.leebeebeom.clothinghelper.data.datasourse.BaseFirebaseDataSource
import com.leebeebeom.clothinghelper.domain.model.data.Todo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoFirebaseDataSource @Inject constructor() : BaseFirebaseDataSource<Todo>()