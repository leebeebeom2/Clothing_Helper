package com.leebeebeom.clothinghelper.main.base

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.TAG
import com.leebeebeom.clothinghelper.map.StableSubCategory
import com.leebeebeom.clothinghelper.map.StableUser
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.AddSubCategoryUseCase
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.EditSubCategoryNameUseCase
import kotlinx.coroutines.launch

abstract class EditSubCategoryNameViewModel(
    private val editSubCategoryNameUseCase: EditSubCategoryNameUseCase,
    addSubCategoryUseCase: AddSubCategoryUseCase
) : AddSubCategoryViewModel(addSubCategoryUseCase) {
    fun editSubCategoryName( // TODO 카테고리 하나 받아서 셋 벨류로 변경
        subCategory: StableSubCategory, newName: String, parent: SubCategoryParent
    ) {
        viewModelScope.launch {
            user?.let { user ->
                val result = editSubCategoryNameUseCase(
                    parent = parent, key = subCategory.key, newName = newName.trim(), uid = user.uid
                )

                if (result is FirebaseResult.Fail) {
                    showToast(R.string.add_category_failed)
                    Log.d(TAG, "taskFailed: $result")
                }
            } ?: showToast(R.string.add_category_failed)
        }
    }
}

abstract class AddSubCategoryViewModel(
    private val addSubCategoryUseCase: AddSubCategoryUseCase,
) : ViewModel() {

    protected abstract val user: StableUser?
    protected abstract fun showToast(@StringRes text: Int)

    fun addSubCategory(name: String, subCategoryParent: SubCategoryParent) {
        viewModelScope.launch {
            user?.let {
                val result = addSubCategoryUseCase(
                    subCategoryParent = subCategoryParent, name = name.trim(), uid = it.uid
                )

                if (result is FirebaseResult.Fail) {
                    showToast(R.string.add_category_failed)
                    Log.d(TAG, "taskFailed: $result")
                }
            } ?: showToast(R.string.add_category_failed)
        }
    }
}