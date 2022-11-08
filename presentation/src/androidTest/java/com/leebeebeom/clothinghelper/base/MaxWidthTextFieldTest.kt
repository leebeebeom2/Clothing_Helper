package com.leebeebeom.clothinghelper.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.leebeebeom.clothinghelper.R
import org.junit.Rule
import org.junit.Test

class MaxWidthTextFieldTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun maxWidthTextFieldTest() { // 세이버블 테스트 불가
        rule.setContent { TestTextField() }

        val field = rule.onNodeWithText("이메일")
        field.assertExists()

        val text = "ㅎㅇ"
        field.performTextInput(text)
        rule.onNodeWithText(text).assertExists()
        field.performTextClearance()

        val error = rule.onNodeWithText("알 수 없는 이유로 실패하였습니다.")
        field.performTextInput("에러")
        error.assertExists()
        field.performTextClearance()
        error.assertDoesNotExist()
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun TestTextField() {
        val textState = rememberSaveable { mutableStateOf("") }
        var textField by remember { mutableStateOf(TextFieldValue(textState.value)) }
        val errorState: MutableState<Int?> = rememberSaveable { mutableStateOf(null) }

        val state = rememberMaxWidthTextFiledState(
            textFieldValue = textField,
            label = R.string.email
        )

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            MaxWidthTextField(
                state = state,
                error = errorState.value,
                onValueChange = {
                    textState.value = it.text
                    textField = it
                    errorState.value = null
                    if (textField.text == "에러")
                        errorState.value = R.string.unknown_error
                },
                onFocusChanged = {
                    textField = textField.copy(
                        selection = TextRange(
                            start = 0,
                            end = textField.text.length
                        )
                    )
                }
            )
        }
    }
}