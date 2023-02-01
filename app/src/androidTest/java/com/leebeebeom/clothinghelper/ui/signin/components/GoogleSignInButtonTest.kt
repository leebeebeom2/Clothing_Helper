package com.leebeebeom.clothinghelper.ui.signin.components

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import com.leebeebeom.clothinghelper.ui.signin.ui.SignInNavViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GoogleSignInButtonTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    lateinit var enabled: MutableState<Boolean>

    @Before
    fun init() {
        enabled = mutableStateOf(true)

        rule.setContent {
            val viewModel: SignInNavViewModel = hiltViewModel()

            GoogleSignInButton(
                enabled = { enabled.value },
                onActivityResult = { viewModel.signInWithGoogleEmail(it) {} },
                disable = { enabled.value = false }
            )
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
        rule.waitUntil {
            rule.onAllNodesWithText("구글 이메일로 시작하기").fetchSemanticsNodes().isNotEmpty()
        }
        assert(FirebaseAuth.getInstance().currentUser != null)
    }

    private val googleSignInButton = rule.onNodeWithText("구글 이메일로 시작하기")
}