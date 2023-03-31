package com.leebeebeom.clothinghelper.domain.repository

import com.leebeebeom.clothinghelper.domain.model.BaseContainerModel
import com.leebeebeom.clothinghelper.ui.drawer.content.MainCategoryType
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.coroutines.flow.SharedFlow

interface BaseContainerRepository<T : BaseContainerModel> : BaseDataRepository<T> {
    val allDataNames: SharedFlow<ImmutableMap<MainCategoryType, ImmutableSet<String>>>
    val allDataSizeFlow: SharedFlow<ImmutableMap<MainCategoryType, Int>>
}