package com.leebeebeom.clothinghelper.data.repository.container

import com.leebeebeom.clothinghelper.data.repository.DatabasePath
import com.leebeebeom.clothinghelper.data.repository.util.NetworkChecker
import com.leebeebeom.clothinghelper.di.AppScope
import com.leebeebeom.clothinghelper.domain.model.DatabaseSubCategory
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SubCategoryPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubCategoryRepositoryImpl @Inject constructor(
    @SubCategoryPreferencesRepository subCategoryPreferencesRepository: SortPreferenceRepository,
    @AppScope appScope: CoroutineScope,
    networkChecker: NetworkChecker,
) : BaseContainerRepositoryImpl<DatabaseSubCategory>(
    sortFlow = subCategoryPreferencesRepository.sort,
    refPath = DatabasePath.SUB_CATEGORIES,
    appScope = appScope,
    networkChecker = networkChecker
), SubCategoryRepository
