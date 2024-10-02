package com.shinlee.showplus.ui.screens.authentication.term

data class Term(
    val id: Int,
    val title: Int,
    val descriptor: Int? = null,
    val isRequired: Boolean,
)