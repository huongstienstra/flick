package com.shinlee.common.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import com.shinlee.common.R

// Fix font size here if need it
object ShowUpTypography {
    private val NunitoSans = FontFamily(
        Font(R.font.nunito_sans_extra_light, FontWeight.ExtraLight),
        Font(R.font.nunito_sans_extra_light_italic, FontWeight.ExtraLight, FontStyle.Italic),
        Font(R.font.nunito_sans_light, FontWeight.Light),
        Font(R.font.nunito_sans_light_italic, FontWeight.Light, FontStyle.Italic),
        Font(R.font.nunito_sans_regular, FontWeight.Normal),
        Font(R.font.nunito_sans_italic, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.nunito_sans_semi_bold, FontWeight.SemiBold),
        Font(R.font.nunito_sans_semi_bold_italic, FontWeight.SemiBold, FontStyle.Italic),
        Font(R.font.nunito_sans_bold, FontWeight.Bold),
        Font(R.font.nunito_sans_bold_italic, FontWeight.Bold, FontStyle.Italic),
        Font(R.font.nunito_sans_extra_bold, FontWeight.ExtraBold),
        Font(R.font.nunito_sans_extra_bold_italic, FontWeight.ExtraBold, FontStyle.Italic)
    )

    val typography = Typography(
        displayLarge = TextStyle(
            fontFamily = NunitoSans,
            fontWeight = FontWeight.Light,
            fontSize = 57.sp,
            lineHeight = 64.sp,
            letterSpacing = (-0.25).sp
        ),
        displayMedium = TextStyle(
            fontFamily = NunitoSans,
            fontWeight = FontWeight.Light,
            fontSize = 45.sp,
            lineHeight = 52.sp
        ),
        displaySmall = TextStyle(
            fontFamily = NunitoSans,
            fontWeight = FontWeight.Normal,
            fontSize = 36.sp,
            lineHeight = 44.sp
        ),
        headlineLarge = TextStyle(
            fontFamily = NunitoSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 40.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = NunitoSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
            lineHeight = 36.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = NunitoSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 32.sp
        ),
        titleLarge = TextStyle(
            fontFamily = NunitoSans,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            lineHeight = 28.sp
        ),
        titleMedium = TextStyle(
            fontFamily = NunitoSans,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.1.sp
        ),
        titleSmall = TextStyle(
            fontFamily = NunitoSans,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = NunitoSans,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = NunitoSans,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp
        ),
        bodySmall = TextStyle(
            fontFamily = NunitoSans,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp
        ),
        labelLarge = TextStyle(
            fontFamily = NunitoSans,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),
        labelMedium = TextStyle(
            fontFamily = NunitoSans,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        ),
        labelSmall = TextStyle(
            fontFamily = NunitoSans,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
    )
}