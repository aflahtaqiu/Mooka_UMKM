package com.example.mooka_umkm.network.model

/**
 *
 * Created by aflah on 27/11/19
 * Email  : aflahtaqiusondha@gmail.com
 * Github : https://github.com/aflahtaqiu
 */


data class MessageChat (
    val id:Long = 0,
    val isi:String = "",
    val nama_pemilik:String = "",
    val nama_toko:String = ""
)