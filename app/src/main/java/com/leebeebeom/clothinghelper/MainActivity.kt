package com.leebeebeom.clothinghelper

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.ui.signin.SignInActivity
import com.leebeebeom.clothinghelper.ui.theme.ClothingHelperTheme
import java.time.Duration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClothingHelperTheme {
                MainActivityScreen()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if (FirebaseAuth.getInstance().currentUser == null)
            startActivity(Intent(this, SignInActivity::class.java))
    }
}

@Preview
@Composable
fun MainActivityPreview() {
    MainActivityScreen()
}