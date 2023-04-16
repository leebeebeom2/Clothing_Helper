package com.leebeebeom.clothinghelper.domain.usecase.preference.sort.folder

import com.leebeebeom.clothinghelper.data.repository.preference.Order
import com.leebeebeom.clothinghelper.domain.repository.preference.FolderPreferenceRepository
import javax.inject.Inject

class ChangeFolderOrderUseCase @Inject constructor(private val folderPreferenceRepository: FolderPreferenceRepository) {
    suspend fun changeOrder(order: Order) = folderPreferenceRepository.changeOrder(order = order)
}