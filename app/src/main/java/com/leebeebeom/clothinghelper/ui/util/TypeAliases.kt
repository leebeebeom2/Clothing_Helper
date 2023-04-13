package com.leebeebeom.clothinghelper.ui.util

import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.model.MenuType

typealias AddFolder = (parentKey: String, name: String, menuType: MenuType) -> Unit
typealias EditFolder = (oldFolder: Folder, name: String) -> Unit

typealias ShowToast = (toastText: Int) -> Unit