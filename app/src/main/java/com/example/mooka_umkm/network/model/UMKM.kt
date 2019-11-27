package com.example.mooka_umkm.network.model

import com.example.mooka_customer.network.model.Gambar
import com.example.mooka_customer.network.model.JenisUmkm
import com.example.mooka_umkm.network.lib.DataResponse


data class UMKM(
    val alamat: String,
    val created_at: String,
    val email: String,
    val gambar: Gambar,
    val id: Int,
    val jenis_umkm_id: Int,
    val kota: String,
    val logo: String,
    val nama: String,
    val nama_pemilik: String,
    val siup: Siup,
    val updated_at: String,
    val jenis_umkm: JenisUmkm,
    val phone: String,
    val password:String
) : DataResponse<UMKM> {
    override fun retrieveData(): UMKM = this
}