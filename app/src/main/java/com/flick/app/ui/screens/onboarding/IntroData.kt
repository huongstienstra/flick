package com.flick.app.ui.screens.onboarding

data class KidsData(
    val title: String,
    val rating: Float,
    val desc: String,
    val imgUri: Int
)

/**
 * create Kids List
 * */

val kidsList = listOf(
    KidsData(
        "Sitting Pretty",
        4.0f,
        "All the Children in the word are cute and innocent for like this...All the Children in the word are cute and innocent for like this...",
        com.flick.common.R.drawable.intro
    ),
    KidsData(
        "Love her Expression",
        4.0f,
        "All the Children in the word are cute and innocent for like this... All the Children in the word are cute and innocent for like this...",
        com.flick.common.R.drawable.intro
    ),
    KidsData(
        "Childhood Superman",
        4.0f,
        "All the Children in the word are cute and innocent for like this...All the Children in the word are cute and innocent for like this...",
        com.flick.common.R.drawable.intro
    )

)