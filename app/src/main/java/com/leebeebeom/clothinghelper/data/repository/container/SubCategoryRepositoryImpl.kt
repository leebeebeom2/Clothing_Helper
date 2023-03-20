package com.leebeebeom.clothinghelper.data.repository.container

import com.leebeebeom.clothinghelper.data.repository.DatabasePath
import com.leebeebeom.clothinghelper.di.AppScope
import com.leebeebeom.clothinghelper.di.DispatcherIO
import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.repository.UserRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SubCategoryPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubCategoryRepositoryImpl @Inject constructor(
    @SubCategoryPreferencesRepository subCategoryPreferencesRepository: SortPreferenceRepository,
    @AppScope appScope: CoroutineScope,
    @DispatcherIO dispatcher: CoroutineDispatcher,
    userRepository: UserRepository,
) : BaseContainerRepositoryImpl<SubCategory>(
    sortFlow = subCategoryPreferencesRepository.sortStream,
    refPath = DatabasePath.SUB_CATEGORIES,
    appScope = appScope,
    type = SubCategory::class.java,
    dispatcher = dispatcher,
    userRepository = userRepository
), SubCategoryRepository
