package com.example.ecomapp.models

data class FeaturedProductModel(
    val image: Int?=null,
    val price: String? = null,
    val rating: Float?=null,
    val reviewCounts: String?=null,
    val productName: String? = null
)



data class BestSellersModel(
    val image: Int?=null,
    val price: String? = null,
    val rating: Float?=null,
    val reviewCounts: String?=null,
    val productName: String? = null
)
