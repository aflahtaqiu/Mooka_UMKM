package com.example.mooka_umkm.network.model

import com.example.mooka_umkm.network.lib.DataResponse

data class Order(
    val created_at: String,
    val id: Int,
    val product_id: Int,
    val status: String,
    val umkm_id: Int,
    val updated_at: String,
    val user_id: Int,
    val product: Product
) : DataResponse<Order> {
    override fun retrieveData(): Order = this
}