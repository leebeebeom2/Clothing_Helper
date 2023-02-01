package com.leebeebeom.clothinghelper.ui.signin.components

import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.ui.MainActivity
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInNavViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GoogleSignInButtonTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()
    lateinit var enabled: MutableState<Boolean>

    @Before
    fun init() {
        enabled = mutableStateOf(true)
        rule.activity.setContent {
            val viewModel: SignInNavViewModel = hiltViewModel()

            GoogleSignInButton(
                enabled = { enabled.value },
                onActivityResult = {
                    viewModel.signInWithGoogleEmail(it) {}
                }
            ) {
                enabled.value = false
            }
        }
    }

    @Test
    fun enabledTest() {
        googleSignInButton.performClick()
        rule.waitForIdle()
        googleSignInButton.assertIsNotEnabled()
    }

    @Test
    fun googleSignInTest() {
        googleSignInButton.performClick()
        rule.waitForIdle()
        assert(FirebaseAuth.getInstance().currentUser != null)
    }

    private val googleSignInButton = rule.onNodeWithText("구글 이메일로 시작하기")
}