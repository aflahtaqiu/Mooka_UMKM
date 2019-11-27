package com.example.mooka_umkm.network.model

import com.example.mooka_customer.network.model.Standard
import com.example.mooka_customer.network.model.Thumbnail


data class Siup(
    val standard: Standard,
    val thumbnail: Thumbnail,
    val url: String
)