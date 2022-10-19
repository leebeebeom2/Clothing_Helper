package com.leebeebeom.clothinghelper.data.datasource

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.data.model.SubCategoryParent
import com.leebeebeom.clothinghelper.data.model.SubCategory
import com.leebeebeom.clothinghelper.data.model.User
import com.leebeebeom.clothinghelper.domain.usecase.user.UserInfoUserCase

class SubCategoryRemoteDataSource(private val userInfoUserCase: UserInfoUserCase) {
    private val dataBase = Firebase.database.reference
    private val userRef = dataBase.child(userInfoUserCase.uid.value)

    fun writeInitialData() {
        val user = User(userInfoUserCase.email.value, userInfoUserCase.name.value)
        userRef.child("user info").setValue(user)
        userRef.child("subCategory").setValue(getInitialSubCategories())
    }

    private fun getInitialSubCategories(): List<SubCategory> {
        var timeStamp = System.currentTimeMillis()

        return listOf(
            SubCategory(SubCategoryParent.Top, timeStamp++, "반팔"),
            SubCategory(SubCategoryParent.Top, timeStamp++, "긴팔"),
            SubCategory(SubCategoryParent.Top, timeStamp++, "셔츠"),
            SubCategory(SubCategoryParent.Top, timeStamp++, "반팔 셔츠"),
            SubCategory(SubCategoryParent.Top, timeStamp++, "니트"),
            SubCategory(SubCategoryParent.Top, timeStamp++, "반팔 니트"),
            SubCategory(SubCategoryParent.Top, timeStamp++, "니트 베스트"),
            SubCategory(SubCategoryParent.Bottom, timeStamp++, "데님"),
            SubCategory(SubCategoryParent.Bottom, timeStamp++, "반바지"),
            SubCategory(SubCategoryParent.Bottom, timeStamp++, "슬랙스"),
            SubCategory(SubCategoryParent.Bottom, timeStamp++, "스웻"),
            SubCategory(SubCategoryParent.Bottom, timeStamp++, "나일론"),
            SubCategory(SubCategoryParent.Bottom, timeStamp++, "치노"),
            SubCategory(SubCategoryParent.OUTER, timeStamp++, "코트"),
            SubCategory(SubCategoryParent.OUTER, timeStamp++, "자켓"),
            SubCategory(SubCategoryParent.OUTER, timeStamp++, "바람막이"),
            SubCategory(SubCategoryParent.OUTER, timeStamp++, "항공점퍼"),
            SubCategory(SubCategoryParent.OUTER, timeStamp++, "블루종"),
            SubCategory(SubCategoryParent.OUTER, timeStamp++, "점퍼"),
            SubCategory(SubCategoryParent.OUTER, timeStamp++, "야상"),
            SubCategory(SubCategoryParent.ETC, timeStamp++, "신발"),
            SubCategory(SubCategoryParent.ETC, timeStamp++, "목걸이"),
            SubCategory(SubCategoryParent.ETC, timeStamp++, "팔찌"),
            SubCategory(SubCategoryParent.ETC, timeStamp++, "귀걸이"),
            SubCategory(SubCategoryParent.ETC, timeStamp++, "볼캡"),
            SubCategory(SubCategoryParent.ETC, timeStamp++, "비니"),
            SubCategory(SubCategoryParent.ETC, timeStamp++, "머플러"),
            SubCategory(SubCategoryParent.ETC, timeStamp, "장갑")
        )
    }
}