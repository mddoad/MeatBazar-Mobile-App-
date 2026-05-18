package com.example.meatbazar.models

class Product {
    var productId: String? = null
    var productName: String? = null
    var productPrice: String? = null
    var productDescription: String? = null
    var productImage: String? = null

    constructor()

    constructor(
        productId: String?, productName: String?, productPrice: String?,
        productDescription: String?, productImage: String?
    ) {
        this.productId = productId
        this.productName = productName
        this.productPrice = productPrice
        this.productDescription = productDescription
        this.productImage = productImage
    }
}