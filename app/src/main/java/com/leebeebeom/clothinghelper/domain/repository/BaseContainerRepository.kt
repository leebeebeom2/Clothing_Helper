package com.leebeebeom.clothinghelper.domain.repository

import com.google.common.collect.ImmutableMap
import com.leebeebeom.clothinghelper.domain.model.BaseContainerModel
import com.leebeebeom.clothinghelper.ui.drawer.contents.MainCategoryType
import kotlinx.coroutines.flow.SharedFlow

interface BaseContainerRepository<T : BaseContainerModel> : BaseDataRepository<T> {
    val allDataNames: SharedFlow<ImmutableMap<MainCategoryType, List<String>>>
    val allDataSizeFlow: SharedFlow<ImmutableMap<MainCategoryType, Int>>
}