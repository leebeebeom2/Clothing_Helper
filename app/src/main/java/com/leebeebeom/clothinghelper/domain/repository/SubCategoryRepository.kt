package com.leebeebeom.clothinghelper.domain.repository

import com.leebeebeom.clothinghelper.domain.model.SubCategory
import com.leebeebeom.clothinghelper.ui.drawer.content.MainCategoryType
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.coroutines.flow.SharedFlow

interface SubCategoryRepository : BaseContainerRepository<SubCategory> {
    val allDataNamesMapFlow: SharedFlow<ImmutableMap<MainCategoryType, ImmutableSet<String>>>
    val allDataSizeMapFlow: SharedFlow<ImmutableMap<MainCategoryType, Int>>
}