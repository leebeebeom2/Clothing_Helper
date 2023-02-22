package com.leebeebeom.clothinghelper.data.repository.container

import android.content.Context
import com.leebeebeom.clothinghelper.data.datasourse.subcategory.SubCategoryFirebaseDataSource
import com.leebeebeom.clothinghelper.data.datasourse.subcategory.SubCategoryRoomDataSource
import com.leebeebeom.clothinghelper.data.repository.DatabasePath
import com.leebeebeom.clothinghelper.domain.model.data.SubCategory
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.NetworkPreferenceRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SubCategoryPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubCategoryRepositoryImpl @Inject constructor(
    @SubCategoryPreferencesRepository subCategoryPreferencesRepository: SortPreferenceRepository,
    @ApplicationContext context: Context,
    subCategoryFirebaseDataSource: SubCategoryFirebaseDataSource,
    subCateRoomDataSource: SubCategoryRoomDataSource,
    networkPreferenceRepository: NetworkPreferenceRepository,
) : BaseContainerRepositoryImpl<SubCategory>(
    sortFlow = subCategoryPreferencesRepository.sort,
    refPath = DatabasePath.SUB_CATEGORIES,
    context = context,
    baseFirebaseDataSource = subCategoryFirebaseDataSource,
    baseRoomDataSource = subCateRoomDataSource,
    networkPreferenceRepository = networkPreferenceRepository
), SubCategoryRepository
