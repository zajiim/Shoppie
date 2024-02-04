package com.example.shoppie.utils

sealed class RegisterValidation {
    data object Success : RegisterValidation()
    data class Failed(val message: String) : RegisterValidation()

}

data class RegisterFieldState(
    val username: RegisterValidation,
    val email: RegisterValidation,
    val phoneNumber: RegisterValidation,
    val password: RegisterValidation,
    val confirmPassword: RegisterValidation,
)