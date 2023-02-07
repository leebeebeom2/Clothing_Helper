package com.leebeebeom.clothinghelper.ui.signin.ui.resetpassword

import androidx.hilt.navigation.compose.hiltViewModel
import com.leebeebeom.clothinghelper.*
import com.leebeebeom.clothinghelper.ui.components.*
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ResetPasswordScreenTest {
    @get:Rule
    val rule = activityRule
    private val customTestRule = CustomTestRule(rule)
    private lateinit var viewModel: ResetPasswordViewModel
    private lateinit var uiState: MutableResetPasswordUiState

    private val emailTextField get() = emailTextField(customTestRule)
    private val sendButton get() = sendButton(customTestRule)

    @Before
    fun init() {
        customTestRule.setContent {
            ClothingHelperTheme {
                viewModel = hiltViewModel()
                uiState = viewModel.uiState as MutableResetPasswordUiState
                ResetPasswordScreen(
                    popBackStack = { },
                    viewModel = viewModel,
                    uiState = uiState
                )
            }
        }
    }

    @Test
    fun showKeyboardTest() = showKeyboardTest(textField = emailTextField)

    @Test
    fun errorTest() {
        errorTest(
            rule = customTestRule,
            errorTextField = { emailTextField },
            errorTextRes = R.string.error_invalid_email,
            setError = {
                emailTextField.input(INVALID_EMAIL)
                sendButton.click()
            },
            beforeText = INVALID_EMAIL
        )

        errorTest(
            rule = customTestRule,
            errorTextField = { emailTextField },
            errorTextRes = R.string.error_user_not_found,
            setError = {
                emailTextField.input(NOT_EXIST_EMAIL)
                sendButton.click()
            },
            beforeText = NOT_EXIST_EMAIL
        )
    }

    @Test
    fun inputChangeTest() = inputChangeTest(
        rule = customTestRule,
        textField = { emailTextField },
        input = { uiState.email }
    )

    @Test
    fun cancelIconTest() = cancelIconTest(
        rule = customTestRule,
        cancelIconTextField = { emailTextField },
        looseFocus = { customTestRule.getNodeWithTag(RESET_PASSWORD_SCREEN_TAG) }
    )

    @Test
    fun blockBlankTest() = blockBlankTest(
        rule = customTestRule,
        textField = { emailTextField }
    )


//    @Test TODO 테스트 불가, 커서 테스트 불가
//    fun looseFocusTest() =
//        looseFocusTest(
//            textField = { emailTextField },
//            root = { customTestRule.getNodeWithTag(RESET_PASSWORD_SCREEN_TAG) }
//        )
}