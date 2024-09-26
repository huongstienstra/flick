package com.shinlee.showplus.ui.screens.authentication.term

sealed class TermUIEvent {
    data class PrivacyPolicyCheckBoxClicked(val status:Boolean) : TermUIEvent()
}