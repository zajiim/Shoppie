package com.example.shoppie.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FeaturedProductModel(
    val image: Int?=null,
    val price: String? = null,
    val rating: Float?=null,
    val reviewCounts: String?=null,
    val productName: String? = null
): Parcelable



data class BestSellersModel(
    val image: Int?=null,
    val price: String? = null,
    val rating: Float?=null,
    val reviewCounts: String?=null,
    val productName: String? = null
)
