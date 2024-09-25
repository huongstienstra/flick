package com.shinlee.showplus.ui.screens.onboarding

data class KidsData(

          val title :String,
          val rating : Float,
          val desc :String,
          val imgUri:Int
)

/**
 * create Kids List
 * */

val kidsList = listOf(
    KidsData(
        "Sitting Pretty",
        4.0f,
        "All the Children in the word are cute and innocent for like this...All the Children in the word are cute and innocent for like this...",
        com.shinlee.common.R.drawable.img_onboar1
    ),
    KidsData(
        "Love her Expression",
        4.0f,
        "All the Children in the word are cute and innocent for like this... All the Children in the word are cute and innocent for like this...",
        com.shinlee.common.R.drawable.img_onboar1
    ),
    KidsData(
        "Childhood Superman",
        4.0f,
        "All the Children in the word are cute and innocent for like this...All the Children in the word are cute and innocent for like this...",
        com.shinlee.common.R.drawable.img_onboar1
    )

)