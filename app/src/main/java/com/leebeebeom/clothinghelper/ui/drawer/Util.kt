package com.leebeebeom.clothinghelper.ui.drawer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavBackStackEntry
import com.leebeebeom.clothinghelper.ui.theme.BlackA3
import com.leebeebeom.clothinghelper.ui.util.getCurrentRoute
import com.leebeebeom.clothinghelper.ui.util.getStringArg
import com.leebeebeom.clothinghelper.ui.viewmodel.ParentKey

@Composable
fun rememberDrawerBackgroundColor(currentBackstack: () -> NavBackStackEntry?, route: String) =
    remember(currentBackstack) {
        derivedStateOf {
            if (currentBackstack().getCurrentRoute() == route) BlackA3
            else Color.Transparent
        }
    }

@Composable
fun rememberDrawerBackgroundColor(
    currentBackstack: () -> NavBackStackEntry?,
    route: String,
    parentKey: String
) =
    remember(currentBackstack) {
        derivedStateOf {
            if (currentBackstack().getCurrentRoute() == route &&
                currentBackstack().getStringArg(ParentKey) == parentKey
            ) BlackA3
            else Color.Transparent
        }
    }