package com.leebeebeom.clothinghelper.ui.signin.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.onNodeWithStringRes
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import com.leebeebeom.clothinghelper.waitStringResExist
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GoogleSignInButtonTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val googleSignInButton = rule.onNodeWithStringRes(R.string.starts_with_google_email)

    @Before
    fun init() {
        FirebaseAuth.getInstance().signOut()
        rule.setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                GoogleSignInButton(
                    onResult = { _, _ -> }
                )
            }
        }
    }

    // 구글 로그인 테스트 불가
    @Test
    fun enabledTest() {
        googleSignInButton.assertIsEnabled()
        googleSignInButton.performClick()
        rule.waitStringResExist(R.string.starts_with_google_email)
        googleSignInButton.assertIsNotEnabled()
    }
}