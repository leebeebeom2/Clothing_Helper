package com.leebeebeom.clothinghelper.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.leebeebeom.clothinghelper.R

// Set of Material typography styles to start with
val NotoSansFontFamily = FontFamily(
    Font(R.font.dotum_pro_light, FontWeight.Thin),
    Font(R.font.dotum_pro_light, FontWeight.ExtraLight),
    Font(R.font.dotum_pro_light, FontWeight.Light),
    Font(R.font.dotum_pro_medium, FontWeight.Normal),
    Font(R.font.dotum_pro_medium, FontWeight.Medium),
    Font(R.font.dotum_pro_medium, FontWeight.SemiBold),
    Font(R.font.dotum_pro_bold, FontWeight.Bold),
    Font(R.font.dotum_pro_bold, FontWeight.ExtraBold),
    Font(R.font.dotum_pro_bold, FontWeight.Black)
)

val NanumBarunGothic = FontFamily(
    Font(R.font.nanumbarungothicultralight, FontWeight.Thin),
    Font(R.font.nanumbarungothiclight, FontWeight.ExtraLight),
    Font(R.font.nanumbarungothiclight, FontWeight.Light),
    Font(R.font.nanumbarungothic, FontWeight.Normal),
    Font(R.font.nanumbarungothic, FontWeight.Medium),
    Font(R.font.nanumbarungothic, FontWeight.SemiBold),
    Font(R.font.nanumbarungothicbold, FontWeight.Bold),
    Font(R.font.nanumbarungothicbold, FontWeight.ExtraBold),
    Font(R.font.nanumbarungothicbold, FontWeight.Black)
)

val Dotum = Typography(
    defaultFontFamily = NotoSansFontFamily,
    h1 = TextStyle(
        fontWeight = FontWeight.Light,
        fontSize = 96.sp,
        letterSpacing = (-1.5).sp,
    ),
    h2 = TextStyle(
        fontWeight = FontWeight.Light,
        fontSize = 60.sp,
        letterSpacing = (-0.5).sp
    ),
    h3 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 48.sp,
        letterSpacing = 0.sp
    ),
    h4 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 34.sp,
        letterSpacing = 0.25.sp
    ),
    h5 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        letterSpacing = 0.sp
    ),
    h6 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        letterSpacing = 0.15.sp
    ),
    subtitle1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp
    ),
    subtitle2 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp
    ),
    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp
    ),
    body2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp
    ),
    button = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 1.25.sp
    ),
    caption = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.4.sp
    ),
    overline = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        letterSpacing = 1.5.sp
    )
)

//val Typography = Typography(
//    body1 = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp,
//    )
/* Other default text styles to override
button = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.W500,
    fontSize = 14.sp
),
caption = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp
)
*/
//)