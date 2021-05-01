package com.example.plusapp.model


data class Product(
    var pId: Long = 0,
    var productName: String = "",
    var productPrice: Long = 0,
    var productPoint: Long = 0,
    var productImage: String = "",
    var productBonus: Long = 0
)
