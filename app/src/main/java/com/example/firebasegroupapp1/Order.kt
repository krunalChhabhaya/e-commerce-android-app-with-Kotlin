package com.example.firebasegroupapp1

data class Order(
    var fullName: String? = null,
    var email: String? = null,
    var productName: String? = null,
    var quantity: Int = 0,
    var totalPrice: Double = 0.0,
    var address: String = "",
    var city: String = "",
    var province: String = "",
    var postalCode: String = "",
    var cardHolderName: String = "",
    var cardNumber: String = "",
    var securityCode: String = "",
    var validUntil: String = "",
    var phoneNumber: String = "",
    var productImage: String = ""
)