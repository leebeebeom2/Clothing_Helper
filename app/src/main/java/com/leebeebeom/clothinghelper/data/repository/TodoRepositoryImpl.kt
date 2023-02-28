package com.leebeebeom.clothinghelper.data.repository

import com.leebeebeom.clothinghelper.data.repository.util.NetworkChecker
import com.leebeebeom.clothinghelper.domain.model.DatabaseTodo
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.NetworkPreferenceRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepositoryImpl @Inject constructor(
    networkPreferenceRepository: NetworkPreferenceRepository,
    networkChecker: NetworkChecker
) : BaseDataRepositoryImpl<DatabaseTodo>(
    refPath = DatabasePath.TODOS,
    networkPreferences = networkPreferenceRepository,
    networkChecker = networkChecker
), TodoRepository