package com.leebeebeom.clothinghelperdata.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
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
                for ((index, subCategories) in allMutableSubCategories.withIndex()) root.addSingleValueListener(
                    uid = it.uid,
                    mutableSubCategories = subCategories,
                    subCategoryParent = subCategoryParent[index],
                    onSubCategoriesLoadingDone = onSubCategoriesLoadingDone[index],
                    onSubCategoriesLoadingCancelled = onSubCategoriesLoadingCancelled[index]
                )
            }
        }

    }

    override fun pushInitialSubCategories(uid: String) {
        root.getSubCategoriesRef(uid).setValue(getInitialSubCategories()) // TODO 10000개로 테스트 해보기
    }

    override fun addSubCategory(
        subCategoryParent: SubCategoryParent, name: String, addSubCategoryListener: FirebaseListener
    ) {
        val timeStamp = System.currentTimeMillis()
        val newSubCategory = SubCategory(subCategoryParent, timeStamp, name)

        root.getSubCategoriesRef(user.value!!.uid).push().setValue(newSubCategory)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    addSubCategory(subCategoryParent, newSubCategory)
                    addSubCategoryListener.taskSuccess()
                } else addSubCategoryListener.taskFailed(it.exception)
            }
    }

    private fun addSubCategory(subCategoryParent: SubCategoryParent, newSubCategory: SubCategory) {
        when (subCategoryParent) {
            SubCategoryParent.TOP -> allMutableSubCategories[TOP].addAndAssign(newSubCategory)
            SubCategoryParent.BOTTOM -> allMutableSubCategories[BOTTOM].addAndAssign(newSubCategory)
            SubCategoryParent.OUTER -> allMutableSubCategories[OUTER].addAndAssign(newSubCategory)
            SubCategoryParent.ETC -> allMutableSubCategories[ETC].addAndAssign(newSubCategory)
        }
    }

    private fun getInitialSubCategories(): List<SubCategory> {
        var timeStamp = System.currentTimeMillis()

        return listOf(
            SubCategory(
                SubCategoryParent.TOP, timeStamp++, "반팔"
            ), SubCategory(
                SubCategoryParent.TOP, timeStamp++, "긴팔"
            ), SubCategory(
                SubCategoryParent.TOP, timeStamp++, "셔츠"
            ), SubCategory(
                SubCategoryParent.TOP, timeStamp++, "반팔 셔츠"
            ), SubCategory(
                SubCategoryParent.TOP, timeStamp++, "니트"
            ), SubCategory(
                SubCategoryParent.TOP, timeStamp++, "반팔 니트"
            ), SubCategory(
                SubCategoryParent.TOP, timeStamp++, "니트 베스트"
            ), SubCategory(
                SubCategoryParent.BOTTOM, timeStamp++, "데님"
            ), SubCategory(
                SubCategoryParent.BOTTOM, timeStamp++, "반바지"
            ), SubCategory(
                SubCategoryParent.BOTTOM, timeStamp++, "슬랙스"
            ), SubCategory(
                SubCategoryParent.BOTTOM, timeStamp++, "스웻"
            ), SubCategory(
                SubCategoryParent.BOTTOM, timeStamp++, "나일론"
            ), SubCategory(
                SubCategoryParent.BOTTOM, timeStamp++, "치노"
            ), SubCategory(
                SubCategoryParent.OUTER, timeStamp++, "코트"
            ), SubCategory(
                SubCategoryParent.OUTER, timeStamp++, "자켓"
            ), SubCategory(
                SubCategoryParent.OUTER, timeStamp++, "바람막이"
            ), SubCategory(
                SubCategoryParent.OUTER, timeStamp++, "항공점퍼"
            ), SubCategory(
                SubCategoryParent.OUTER, timeStamp++, "블루종"
            ), SubCategory(
                SubCategoryParent.OUTER, timeStamp++, "점퍼"
            ), SubCategory(
                SubCategoryParent.OUTER, timeStamp++, "야상"
            ), SubCategory(
                SubCategoryParent.ETC, timeStamp++, "신발"
            ), SubCategory(
                SubCategoryParent.ETC, timeStamp++, "목걸이"
            ), SubCategory(
                SubCategoryParent.ETC, timeStamp++, "팔찌"
            ), SubCategory(
                SubCategoryParent.ETC, timeStamp++, "귀걸이"
            ), SubCategory(
                SubCategoryParent.ETC, timeStamp++, "볼캡"
            ), SubCategory(
                SubCategoryParent.ETC, timeStamp++, "비니"
            ), SubCategory(
                SubCategoryParent.ETC, timeStamp++, "머플러"
            ), SubCategory(
                SubCategoryParent.ETC, timeStamp, "장갑"
            )
        )
    }

    private fun DatabaseReference.getUidRef(uid: String) = child(uid)
    private fun DatabaseReference.getSubCategoriesRef(uid: String) =
        getUidRef(uid).child(DatabasePath.SUB_CATEGORIES)

    private fun DatabaseReference.addSingleValueListener(
        uid: String,
        subCategoryParent: SubCategoryParent,
        mutableSubCategories: MutableStateFlow<List<SubCategory>>,
        onSubCategoriesLoadingDone: () -> Unit,
        onSubCategoriesLoadingCancelled: (Int, String) -> Unit
    ) {
        getSubCategoriesRef(uid).orderByChild("parent").equalTo(subCategoryParent.name)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val temp = mutableListOf<SubCategory>()
                    for (child in snapshot.children) {
                        child.getValue<SubCategory>()?.let { temp.add(it) }
                    }
                    mutableSubCategories.value = temp
                    onSubCategoriesLoadingDone()
                }

                override fun onCancelled(error: DatabaseError) =
                    onSubCategoriesLoadingCancelled(error.code, error.message)
            })
    }
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