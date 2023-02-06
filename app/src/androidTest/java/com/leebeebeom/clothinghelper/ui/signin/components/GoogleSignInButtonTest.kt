package com.leebeebeom.clothinghelper.ui.signin.components

import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.CustomTestRule
import com.leebeebeom.clothinghelper.activityRule
import com.leebeebeom.clothinghelper.googleSignInButton
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInNavViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GoogleSignInButtonTest {
    @get:Rule
    val rule = activityRule
    private val customTestRule = CustomTestRule(rule)

    private val googleSignInButton = googleSignInButton(customTestRule)

    @Before
    fun init() {
        FirebaseAuth.getInstance().signOut()

        customTestRule.setContent {
            val viewModel: SignInNavViewModel = hiltViewModel()

            GoogleSignInButton(
                enabled = { viewModel.uiState.googleButtonEnabled },
                onActivityResult = { },
                disable = { viewModel.setGoogleButtonEnable(false) }
            )
        }
    }

    @Test
    fun enabledTest() {
        googleSignInButton.click()
        googleSignInButton.notEnabled(false)
    }

    // 구글 로그인 테스트 불가
}