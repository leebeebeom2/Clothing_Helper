package com.leebeebeom.clothinghelper.data.repository.container

import com.leebeebeom.clothinghelper.data.repository.DatabasePath
import com.leebeebeom.clothinghelper.domain.model.data.SubCategory
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SubCategoryPreferencesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubCategoryRepositoryImpl @Inject constructor(@SubCategoryPreferencesRepository subCategoryPreferencesRepository: SortPreferenceRepository) :
    BaseDataRepositoryImpl<SubCategory>(
        sortFlow = subCategoryPreferencesRepository.sort,
        refPath = DatabasePath.SUB_CATEGORIES
    ), SubCategoryRepository