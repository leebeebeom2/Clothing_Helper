package com.leebeebeom.clothinghelper.ui.signin.components

import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.activityRule
import com.leebeebeom.clothinghelper.googleSignInButton
import com.leebeebeom.clothinghelper.restoreTester
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInNavViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GoogleSignInButtonTest {
    @get:Rule
    val rule = activityRule
    val restoreTester = restoreTester(rule)

    @Before
    fun init() {
        FirebaseAuth.getInstance().signOut()

        restoreTester.setContent {
            val viewModel: SignInNavViewModel = hiltViewModel()

            GoogleSignInButton(
                enabled = { viewModel.uiState.googleButtonEnabled },
                onActivityResult = { viewModel.signInWithGoogleEmail(it) {} },
                disable = { viewModel.setGoogleButtonEnable(false) }
            )
        }
    }

    @Test
    fun enabledTest() {
        rule.googleSignInButton.performClick()
        rule.waitForIdle()
        rule.googleSignInButton.assertIsNotEnabled()
    }

    // 구글 로그인 테스트 불가
}