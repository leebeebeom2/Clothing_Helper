package com.leebeebeom.clothinghelper.main.base

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.TAG
import com.leebeebeom.clothinghelper.map.StableUser
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.usecase.subcategory.AddSubCategoryUseCase
import kotlinx.coroutines.launch

abstract class AddSubCategoryVIewModel(private val addSubCategoryUseCase: AddSubCategoryUseCase) :
    ViewModel() {
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