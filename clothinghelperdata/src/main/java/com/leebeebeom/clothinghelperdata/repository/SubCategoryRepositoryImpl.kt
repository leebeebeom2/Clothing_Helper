package com.leebeebeom.clothinghelperdata.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.model.SubCategory
import com.leebeebeom.clothinghelperdomain.model.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.model.User
import com.leebeebeom.clothinghelperdomain.repository.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

// TODO EXPAND 프리퍼런스로 이동??

class SubCategoryRepositoryImpl : SubCategoryRepository {
    private val root = Firebase.database.reference

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean>
        get() = _isLoading

    private val _allSubCategories = List(4) { MutableStateFlow(emptyList<SubCategory>()) }


    override suspend fun loadSubCategories(
        user: User?,
        onFailed: (Exception) -> Unit
    ) {
        loadingOn()

        user?.let {
            root.getSubCategoriesRef(it.uid).orderByChild("parent")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val temp = List(4) { mutableListOf<SubCategory>() }

                        for (child in snapshot.children) child.getValue<SubCategory>()
                            ?.let { subCategory ->
                                temp[subCategory.parent.ordinal].add(subCategory)
                            }
                        _allSubCategories.forEachIndexed { i, subCategoriesFlow ->
                            subCategoriesFlow.value = temp[i]
                        }

                        loadingOff()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        onFailed(error.toException())

                        loadingOff()
                    }
                })
        }
    }

    override suspend fun getAllSubCategories(
        scope: CoroutineScope,
        sortPreferencesFlow: Flow<SubCategorySortPreferences>
    ): List<StateFlow<List<SubCategory>>> =
        _allSubCategories.map { subCategoriesFlow ->
            sortPreferencesFlow.combine(subCategoriesFlow) { preferences, subCategories ->
                sortSubCategories(preferences, subCategories)
            }.stateIn(scope)
        }

    private fun sortSubCategories(
        preferences: SubCategorySortPreferences,
        subCategories: List<SubCategory>
    ): List<SubCategory> {
        val sort = preferences.sort
        val order = preferences.sortOrder

        return when {
            sort == SubCategorySort.NAME && order == SortOrder.ASCENDING -> subCategories.sortedBy { it.name }
            sort == SubCategorySort.NAME && order == SortOrder.DESCENDING -> subCategories.sortedByDescending { it.name }
            sort == SubCategorySort.CREATE && order == SortOrder.ASCENDING -> subCategories.sortedBy { it.createDate }
            sort == SubCategorySort.CREATE && order == SortOrder.DESCENDING -> subCategories.sortedByDescending { it.createDate }
            else -> subCategories
        }
    }

    override fun pushInitialSubCategories(uid: String) {
        val subCategoryRef = root.getSubCategoriesRef(uid)

        for (subCategory in getInitialSubCategories())
            if (pushSubCategory(subCategoryRef, subCategory) is FirebaseResult.Fail)
                return
    }

    private fun pushSubCategory(
        subCategoryRef: DatabaseReference,
        subCategory: SubCategory
    ): FirebaseResult {
        val key = subCategoryRef.push().key
            ?: return FirebaseResult.Fail(NullPointerException("key 생성 실패"))

        return if (subCategoryRef.child(key).setValue(subCategory.copy(key = key)).isSuccessful)
            FirebaseResult.Success
        else FirebaseResult.Fail(exception = null)
    }

    override fun addSubCategory(
        subCategoryParent: SubCategoryParent,
        name: String,
        uid: String,
        taskFailed: (Exception?) -> Unit
    ) {
        val newSubCategory = SubCategory(
            parent = subCategoryParent,
            name = name,
            createDate = System.currentTimeMillis()
        )

        val subCategoryRef = root.getSubCategoriesRef(uid)
        val result = pushSubCategory(subCategoryRef, newSubCategory)


        if (result is FirebaseResult.Fail)
            taskFailed(result.exception)
        else _allSubCategories[subCategoryParent.ordinal]
            .taskAndAssign { temp -> temp.add(newSubCategory) }
    }


    override fun editSubCategoryName(
        subCategory: SubCategory,
        newName: String,
        uid: String,
        taskFailed: (Exception?) -> Unit
    ) {

        if (root.getSubCategoriesRef(uid).child(subCategory.key).child("name")
                .setValue(newName).isSuccessful
        )
            _allSubCategories[subCategory.parent.ordinal].taskAndAssign {
                val index = it.indexOf(subCategory)
                it.remove(subCategory)
                it.add(index, subCategory.copy(name = newName))
            }
        else taskFailed(null)
    }

    private fun getInitialSubCategories(): List<SubCategory> {
        var timeStamp = System.currentTimeMillis()
        return listOf(
            SubCategory(
                parent = SubCategoryParent.TOP, name = "반팔", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.TOP, name = "긴팔", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.TOP, name = "셔츠", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.TOP, name = "반팔 셔츠", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.TOP, name = "니트", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.TOP, name = "반팔 니트", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.TOP, name = "니트 베스트", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM, name = "데님", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM, name = "반바지", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM, name = "슬랙스", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM, name = "스웻", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM, name = "나일론", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.BOTTOM, name = "치노", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "코트", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "자켓", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "바람막이", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "항공점퍼", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "블루종", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "점퍼", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.OUTER, name = "야상", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "신발", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "목걸이", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "팔찌", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "귀걸이", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "볼캡", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "비니", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "머플러", createDate = timeStamp++
            ), SubCategory(
                parent = SubCategoryParent.ETC, name = "장갑", createDate = timeStamp
            )
        )
    }

    private fun loadingOn() {
        _isLoading.value = true
    }

    private fun loadingOff() {
        _isLoading.value = true
    }

    private fun DatabaseReference.getSubCategoriesRef(uid: String) =
        child(uid).child(DatabasePath.SUB_CATEGORIES)
}

object DatabasePath {
    const val SUB_CATEGORIES = "sub categories"
    const val USER_INFO = "user info"
}

inline fun <T> MutableStateFlow<List<T>>.taskAndAssign(crossinline task: (MutableList<T>) -> Unit) {
    val temp = this.value.toMutableList()
    task(temp)
    this.value = temp
}