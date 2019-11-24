package com.example.mooka_umkm.network.model

import com.example.mooka_umkm.network.lib.DataResponse
import com.pens.managementmasyrakat.network.lib.DataResponse

data class ListResponse<T>(val items: List<T>) : DataResponse<List<T>> {
    override fun retrieveData(): List<T> = items
}