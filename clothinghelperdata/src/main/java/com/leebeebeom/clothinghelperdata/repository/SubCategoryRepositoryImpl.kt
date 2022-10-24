package com.leebeebeom.clothinghelperdata.repository

import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelperdata.repository.SubCategoryParentIndex.BOTTOM
import com.leebeebeom.clothinghelperdata.repository.SubCategoryParentIndex.ETC
import com.leebeebeom.clothinghelperdata.repository.SubCategoryParentIndex.OUTER
import com.leebeebeom.clothinghelperdata.repository.SubCategoryParentIndex.TOP
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.FirebaseListener
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelperdomain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object SubCategoryParentIndex {
    const val TOP = 0
    const val BOTTOM = 1
    const val OUTER = 2
    const val ETC = 3
}

class SubCategoryRepositoryImpl(private val userRepository: UserRepository) :
    SubCategoryRepository {
    private val root = Firebase.database.reference
    private val user = MutableStateFlow(userRepository.user.value)

    private val allMutableSubCategories = listOf(
        MutableStateFlow(emptyList<SubCategory>()),
        MutableStateFlow(emptyList()),
        MutableStateFlow(emptyList()),
        MutableStateFlow(emptyList())
    )

    private val subCategoryParent = enumValues<SubCategoryParent>()

    override val topSubCategories: StateFlow<List<SubCategory>>
        get() = allMutableSubCategories[TOP]
    override val bottomSubCategories: StateFlow<List<SubCategory>>
        get() = allMutableSubCategories[BOTTOM]
    override val outerSubCategories: StateFlow<List<SubCategory>>
        get() = allMutableSubCategories[OUTER]
    override val etcSubCategories: StateFlow<List<SubCategory>>
        get() = allMutableSubCategories[ETC]

    override suspend fun loadSubCategories(
        onSubCategoriesLoadingDone: List<() -> Unit>,
        onSubCategoriesLoadingCancelled: List<(errorCode: Int, message: String) -> Unit>
    ) {
        userRepository.user.collect {
            this.user.value = it
            it?.let {
                val query = root.getSubCategoriesRef(it.uid).orderByChild("parent")

                for ((index, subCategories) in allMutableSubCategories.withIndex())
                    addSingleValueListener(
                        query = query,
                        subCategoryParent = subCategoryParent[index],
                        mutableSubCategories = subCategories,
                        onSubCategoriesLoadingDone = onSubCategoriesLoadingDone[index],
                        onSubCategoriesLoadingCancelled = onSubCategoriesLoadingCancelled[index]
                    )
            }
        }

    }

    private fun addSingleValueListener(
        query: Query,
        subCategoryParent: SubCategoryParent,
        mutableSubCategories: MutableStateFlow<List<SubCategory>>,
        onSubCategoriesLoadingDone: () -> Unit,
        onSubCategoriesLoadingCancelled: (Int, String) -> Unit
    ) {
        query.equalTo(subCategoryParent.name)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val temp = mutableListOf<SubCategory>()
                    for (child in snapshot.children)
                        child.getValue<SubCategory>()?.let { temp.add(it) }

                    mutableSubCategories.value = temp
                    onSubCategoriesLoadingDone()
                }

                override fun onCancelled(error: DatabaseError) =
                    onSubCategoriesLoadingCancelled(error.code, error.message)
            })
    }

    override fun pushInitialSubCategories(uid: String) {
        val subCategoryRef = root.getSubCategoriesRef(uid)

        for (subCategory in getInitialSubCategories()) pushInitialData(subCategoryRef, subCategory)
    }

    private fun pushInitialData(subCategoryRef: DatabaseReference, subCategory: SubCategory) {
        val key = subCategoryRef.push().key ?: return // 실패 처리

        subCategoryRef.child(key).setValue(subCategory.copy(key = key))
            .addOnFailureListener { } // TODO 실패 처리
    }

    override fun addSubCategory(
        subCategoryParent: SubCategoryParent, name: String, addSubCategoryListener: FirebaseListener
    ) {
        val key = root.getSubCategoriesRef(user.value!!.uid).push().key
        if (key == null) {
            addSubCategoryListener.taskFailed(NullPointerException("키 생성 실패"))
            return
        }

        val newSubCategory = SubCategory(subCategoryParent, key, name)

        root.getSubCategoriesRef(user.value!!.uid).child(key).setValue(newSubCategory)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    when (subCategoryParent) {
                        SubCategoryParent.TOP -> allMutableSubCategories[TOP].addAndAssign(
                            newSubCategory
                        )
                        SubCategoryParent.BOTTOM -> allMutableSubCategories[BOTTOM].addAndAssign(
                            newSubCategory
                        )
                        SubCategoryParent.OUTER -> allMutableSubCategories[OUTER].addAndAssign(
                            newSubCategory
                        )
                        SubCategoryParent.ETC -> allMutableSubCategories[ETC].addAndAssign(
                            newSubCategory
                        )
                    }
                    addSubCategoryListener.taskSuccess()
                } else addSubCategoryListener.taskFailed(it.exception)
            }
    }

    override fun deleteSubCategory(
        subCategory: SubCategory,
        deleteSubCategoryListener: FirebaseListener
    ) {
        root.getSubCategoriesRef(user.value!!.uid).child(subCategory.key).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    when (subCategory.parent) {
                        SubCategoryParent.TOP -> allMutableSubCategories[TOP].removeAndAssign(
                            subCategory
                        )
                        SubCategoryParent.BOTTOM -> allMutableSubCategories[BOTTOM].removeAndAssign(
                            subCategory
                        )
                        SubCategoryParent.OUTER -> allMutableSubCategories[OUTER].removeAndAssign(
                            subCategory
                        )
                        SubCategoryParent.ETC -> allMutableSubCategories[ETC].removeAndAssign(
                            subCategory
                        )
                    }
                    deleteSubCategoryListener.taskSuccess()
                } else deleteSubCategoryListener.taskFailed(it.exception)
            }
    }

    private fun getInitialSubCategories(): List<SubCategory> {
        return listOf(
            SubCategory(
                parent = SubCategoryParent.TOP, name = "반팔"
            ), SubCategory(
                parent = SubCategoryParent.TOP, name = "긴팔"
            ), SubCategory(
                parent = SubCategoryParent.TOP, name = "셔츠"
            ), SubCategory(
                parent = SubCategoryParent.TOP, name = "반팔 셔츠"
            ), SubCategory(
                parent = SubCategoryParent.TOP, name = "니트"
            ), SubCategory(
                parent = SubCategoryParent.TOP, name = "반팔 니트"
            ), SubCategory(
                parent = SubCategoryParent.TOP, name = "니트 베스트"
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM, name = "데님"
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM, name = "반바지"
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM, name = "슬랙스"
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM, name = "스웻"
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM, name = "나일론"
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM, name = "치노"
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "코트"
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "자켓"
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "바람막이"
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "항공점퍼"
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "블루종"
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "점퍼"
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "야상"
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "신발"
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "목걸이"
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "팔찌"
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "귀걸이"
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "볼캡"
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "비니"
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "머플러"
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "장갑"
            )
        )
    }

    private fun DatabaseReference.getSubCategoriesRef(uid: String) =
        child(uid).child(DatabasePath.SUB_CATEGORIES)
}

object DatabasePath {
    const val SUB_CATEGORIES = "sub categories"
    const val USER_INFO = "user info"
}

fun <T> MutableStateFlow<List<T>>.addAndAssign(value: T) {
    val temp = this.value.toMutableList()
    temp.add(value)
    this.value = temp
}

fun <T> MutableStateFlow<List<T>>.removeAndAssign(value: T) {
    val temp = this.value.toMutableList()
    temp.remove(value)
    this.value = temp
}