package com.example.mooka_umkm.network.model

import com.example.mooka_umkm.network.lib.DataResponse

data class Inbox(
    val created_at: String,
    val id: Int,
    val judul: String,
    val text: String,
    val tipe: String,
    val umkm_id: Int,
    val updated_at: String
) : DataResponse<Inbox> {
    override fun retrieveData(): Inbox = this
}