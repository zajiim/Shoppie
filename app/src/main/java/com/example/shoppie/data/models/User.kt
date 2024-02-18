package com.example.shoppie.data.models

data class User(
    val userName: String,
    val email: String?="",
    val phoneNumber: String?="",
    val imagePath :String ?= ""
)
