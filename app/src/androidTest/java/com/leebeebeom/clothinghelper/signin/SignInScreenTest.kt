package com.leebeebeom.clothinghelper.signin


import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.ui.main.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

const val INVALID_EMAIL = "invalid email"
const val FAKE_EMAIL = "fake@fake.com"
const val FAKE_PASSWORD = "fakePassword"
const val EMAIL = "1@a.com"
const val PASSWORD = "111111"

class SignInScreenTest {
    //TODO 뒤로가기 버튼 테스트 구글 로그인 테스트, 로그인 테스트

    @get:Rule
    val signInScreenTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() = signInScreenTestRule.waitUntilExists(hasText(getString(R.string.login)))

    @Test
    fun emptyTests() {
        allEmpty()
        emailNotEmpty()
        passwordEmpty()
        allNotEmpty()
    }

    private fun allNotEmpty() {
        email.performTextInput("a")
        password.performTextInput("a")
        loginBtn.assertIsEnabled()
    }

    private fun passwordEmpty() {
        email.performTextInput("a")
        loginBtn.assertIsNotEnabled()
        email.performTextClearance()
    }

    private fun emailNotEmpty() {
        email.performTextInput(EMAIL)
        loginBtn.assertIsNotEnabled()
        email.performTextClearance()
    }

    private fun allEmpty() {
        loginBtn.assertIsNotEnabled()
    }

    @Test
    fun passwordVisibilityTest() {
        invisible()
        onVisible()
        onInvisible()
    }

    private fun onInvisible() {
        visibleIcon.performClick()
        signInScreenTestRule.onNodeWithText("•").assertExists()
    }

    private fun onVisible() {
        visibleIcon.performClick()
        signInScreenTestRule.onNodeWithText("a").assertExists()
    }

    private fun invisible() {
        password.performTextInput("a")
        signInScreenTestRule.onNodeWithText("•").assertExists()
    }

    @Test
    fun navigateSignUpTest() {
        emailSignUp.performClick()
        signInScreenTestRule.onNodeWithText(getString(R.string.sign_up)).assertExists()

    }

    @Test
    fun navigateResetPasswordTest() {
        forgotPassword.performClick()
        signInScreenTestRule.onNodeWithText(getString(R.string.check)).assertExists()
    }

    @Test
    fun loginTest() {
        invalidEmail()
        userNotFound()
        wrongPassword()
//        login() 테스트 불가
    }


    private fun login() {
        email.performTextInput(EMAIL)
        password.performTextInput(PASSWORD)
        loginBtn.performClick()
    }


    private fun wrongPassword() {
        email.performTextInput(EMAIL)
        password.performTextInput(FAKE_PASSWORD)
        loginBtn.performClick()
        signInScreenTestRule.waitUntilExists(hasText(getString(R.string.error_wrong_password)))
        signInScreenTestRule.onNodeWithText(getString(R.string.error_wrong_password)).assertExists()
        textClear()
    }

    private fun userNotFound() {
        email.performTextInput(FAKE_EMAIL)
        password.performTextInput(FAKE_PASSWORD)
        loginBtn.performClick()
        signInScreenTestRule.waitUntilExists(hasText(getString(R.string.error_user_not_found)))
        signInScreenTestRule.onNodeWithText(getString(R.string.error_user_not_found)).assertExists()
        textClear()
    }

    private fun invalidEmail() {
        email.performTextInput(INVALID_EMAIL)
        password.performTextInput(FAKE_PASSWORD)
        loginBtn.performClick()
        signInScreenTestRule.waitUntilExists(hasText(getString(R.string.error_invalid_email)))
        signInScreenTestRule.onNodeWithText(getString(R.string.error_invalid_email)).assertExists()
        textClear()
    }

    private fun textClear() {
        email.performTextClearance()
        password.performTextClearance()
    }

    private val visibleIcon
        get() = signInScreenTestRule.onNode(hasContentDescription("visible icon"))

    private val emailSignUp
        get() = signInScreenTestRule.onNodeWithText(getString(R.string.sign_up_with_email))

    private val forgotPassword
        get() = signInScreenTestRule.onNodeWithText(getString(R.string.forget_password))

    private val password
        get() = signInScreenTestRule.onNodeWithText(getString(R.string.password))

    private val email
        get() = signInScreenTestRule.onNodeWithText(getString(R.string.email))

    private val loginBtn
        get() = signInScreenTestRule.onNodeWithText(getString(R.string.login))

    private fun getString(resId: Int) =
        signInScreenTestRule.activity.getString(resId)
}

fun <T : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<T>, T>.waitUntilNodeCount(
    matcher: SemanticsMatcher,
    count: Int,
    timeoutMillis: Long = 5_000L
) {
    this.waitUntil(timeoutMillis) {
        this.onAllNodes(matcher).fetchSemanticsNodes().size == count
    }
}


fun <T : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<T>, T>.waitUntilExists(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = 5_000L
) {
    return this.waitUntilNodeCount(matcher, 1, timeoutMillis)
}

fun <T : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<T>, T>.waitUntilDoesNotExist(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = 5_000L
) {
    return this.waitUntilNodeCount(matcher, 0, timeoutMillis)
}