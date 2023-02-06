package com.leebeebeom.clothinghelper

import com.leebeebeom.clothinghelper.R.string.*
import com.leebeebeom.clothinghelper.ui.components.CANCEL_ICON_TAG
import com.leebeebeom.clothinghelper.ui.components.CENTER_DOT_PROGRESS_INDICATOR_TAG
import com.leebeebeom.clothinghelper.ui.signin.components.textfield.INVISIBLE_ICON_TAG
import com.leebeebeom.clothinghelper.ui.signin.components.textfield.VISIBLE_ICON_TAG

fun emailTextField(rule: CustomTestRule) = rule.getNodeWithStringRes(email)
fun passwordTextField(rule: CustomTestRule) = rule.getNodeWithStringRes(password)
fun nickNameTextField(rule: CustomTestRule) = rule.getNodeWithStringRes(nickname)
fun passwordConfirmTextField(rule: CustomTestRule) = rule.getNodeWithStringRes(password_confirm)
fun signUpButton(rule: CustomTestRule) = rule.getNodeWithStringRes(sign_up)
fun sendButton(rule: CustomTestRule) = rule.getNodeWithStringRes(send)
fun signInButton(rule: CustomTestRule) = rule.getNodeWithStringRes(sign_in)
fun centerDotProgressIndicator(rule: CustomTestRule) =
    rule.getNodeWithTag(CENTER_DOT_PROGRESS_INDICATOR_TAG)

fun checkButton(rule: CustomTestRule) = rule.getNodeWithStringRes(check)
fun visibleIcon(rule: CustomTestRule) = rule.getNodeWithTag(VISIBLE_ICON_TAG)
fun invisibleIcon(rule: CustomTestRule) = rule.getNodeWithTag(INVISIBLE_ICON_TAG)
fun cancelIcon(rule: CustomTestRule) = rule.getNodeWithTag(CANCEL_ICON_TAG)
fun googleSignInButton(rule: CustomTestRule) = rule.getNodeWithStringRes(starts_with_google_email)