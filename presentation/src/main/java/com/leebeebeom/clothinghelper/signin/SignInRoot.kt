package com.leebeebeom.clothinghelper.signin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leebeebeom.clothinghelper.base.CenterDotProgressIndicator
import com.leebeebeom.clothinghelper.theme.ClothingHelperTheme
import com.leebeebeom.clothinghelperdomain.usecase.user.GetSignInLoadingStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SignInRoot(
    viewModel: SignInRootViewModel = hiltViewModel(),
    content: @Composable (BoxScope.() -> Unit)
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val isLoading by viewModel.getSignInLoadingStateUseCase().collectAsStateWithLifecycle()

    ClothingHelperTheme {
        Scaffold {
            Surface {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .padding(horizontal = 20.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = focusManager::clearFocus
                        ),
                ) {
                    content()
                }
            }
        }
    }
    CenterDotProgressIndicator(isLoading = { isLoading })
}

@HiltViewModel
class SignInRootViewModel @Inject constructor(val getSignInLoadingStateUseCase: GetSignInLoadingStateUseCase) :
    ViewModel()