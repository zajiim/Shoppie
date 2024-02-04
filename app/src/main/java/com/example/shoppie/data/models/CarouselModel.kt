package com.example.ecomapp.models


data class CarouselModel(
    val id: String?,
    val image: Int?=null,
    val drawable: Int?=null,
    val title: String?=null,
    val subtitle: String?=null,
)

data class CarouselModel2(
    val id: String?,
    val image: Int?=null,
    val drawable: Int?=null,
    val title: String?=null,
    val subtitle: String?=null,
    val color: Int,
)


