package com.example.shoppie.utils

import android.util.Patterns

fun validateEmail(email: String): RegisterValidation {
    if (email.isEmpty()) {
        return RegisterValidation.Failed("Email can't be empty")
    }

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        return RegisterValidation.Failed("Wrong email format")
    }

    return RegisterValidation.Success
}

fun validatePassword(password: String): RegisterValidation {
    if (password.isEmpty()) {
        return RegisterValidation.Failed("Password can't be empty")
    }
    if (password.length < 6) {
        return RegisterValidation.Failed("Password must contain at least 6 letters")
    }

    return RegisterValidation.Success
}

fun validateUserName(userName: String): RegisterValidation {
    if (userName.isEmpty()) {
        return RegisterValidation.Failed("Username can't be empty")
    }

    return RegisterValidation.Success
}

fun validateConfirmPassword(password: String, confirmPassword: String): RegisterValidation {
    if (password != confirmPassword) {
        return RegisterValidation.Failed("Passwords do not match")
    }

    return RegisterValidation.Success
}

fun validatePhoneNumber(phoneNumber: String): RegisterValidation {
    if (phoneNumber.isEmpty()) {
        return RegisterValidation.Failed("Phone number can't be empty")
    }

    return RegisterValidation.Success
}
