package com.example.shoppie.utils

sealed class RegisterValidation {
    data object Success : RegisterValidation()
    data class Failed(val message: String) : RegisterValidation()

}

data class RegisterFieldState(
    val username: RegisterValidation?=null,
    val email: RegisterValidation?=null,
    val phoneNumber: RegisterValidation?=null,
    val password: RegisterValidation?=null,
    val confirmPassword: RegisterValidation?=null,
)