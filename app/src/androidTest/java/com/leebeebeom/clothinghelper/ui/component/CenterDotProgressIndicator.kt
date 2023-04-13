package com.leebeebeom.clothinghelper.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.leebeebeom.clothinghelper.ui.HiltTestActivity
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import com.leebeebeom.clothinghelper.ui.waitTagExist
import com.leebeebeom.clothinghelper.ui.waitTagNotExist
import com.leebeebeom.clothinghelper.ui.waitTextExist
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CenterDotProgressIndicatorTest {
    @get:Rule
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    private val screen1Route = "screen 1"
    private val screen2Route = "screen 2"
    private var isLoading by mutableStateOf(false)

    @Before
    fun init() {
        rule.setContent {
            ClothingHelperTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = screen1Route) {
                    composable(screen1Route) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(text = screen1Route)
                        }
                    }
                    composable(screen2Route) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(text = screen2Route)

                            CenterDotProgressIndicator(show = { isLoading })
                        }
                    }
                }
                navController.navigate(screen2Route)
            }
        }
    }

    @Test
    fun blockBackPressTest() {
        rule.waitTextExist(screen2Route)

        isLoading = true

        rule.waitTagExist(CenterDotProgressIndicatorTag)
        repeat(5) { device.pressBack() }

        isLoading = false

        rule.waitTagNotExist(CenterDotProgressIndicatorTag)
        device.pressBack()
        rule.waitTextExist(screen1Route)
    }
}