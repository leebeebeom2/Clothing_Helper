package com.leebeebeom.clothinghelper.data.datasourse.todo

import com.leebeebeom.clothinghelper.data.datasourse.BaseFirebaseDataSource
import com.leebeebeom.clothinghelper.data.repository.DatabasePath
import com.leebeebeom.clothinghelper.domain.model.FirebaseTodo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoFirebaseDataSource @Inject constructor() :
    BaseFirebaseDataSource<FirebaseTodo>(refPath = DatabasePath.TODOS)