package com.example.mooka_umkm.network.model

import com.example.mooka_umkm.network.lib.DataResponse

data class Community(
    val banner: Banner,
    val created_at: String,
    val id: Int,
    val member_count: Int,
    val official: Boolean,
    val subtitle: String,
    val title: String,
    val updated_at: String
) : DataResponse<Community> {
    override fun retrieveData(): Community = this
}