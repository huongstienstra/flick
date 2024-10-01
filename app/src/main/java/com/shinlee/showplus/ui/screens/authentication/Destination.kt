package com.shinlee.showplus.ui.screens.authentication

sealed class Destination(val route: String) {
    data object LoginFragment : Destination("login_fragment")
    data object SignUpFragment : Destination("signup_fragment")
    data object TermFragment : Destination("term_fragment")
    data object FirstStepSignup : Destination("first_step_signup_fragment")
}