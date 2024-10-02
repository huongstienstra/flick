package com.nativemobilebits.loginflow.data.rules

object Validator {

     fun validateEmail(email: String): String? {
        return if (email.isEmpty()) {
            "Email cannot be empty"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Please enter valid email address"
        } else {
            null
        }
    }

     fun validatePassword(password: String): String? {
        return when {
            password.isEmpty() -> "Password cannot be empty"
            password.length < 8 -> "Password must be at least 8 characters"
//            !password.any { it.isDigit() } -> "Password must contain at least one number"
//            !password.any { it.isLetter() } -> "Password must contain at least one letter"
            else -> null
        }
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): String? {
        return when {
            password != confirmPassword -> "Your password no correct"
            else -> null
        }
    }

}








