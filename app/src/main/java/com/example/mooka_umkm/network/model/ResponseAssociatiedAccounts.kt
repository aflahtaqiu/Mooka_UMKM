package com.example.mooka_umkm.network.model

import com.example.mooka_umkm.network.lib.DataResponse

data class ResponseAssociatiedAccounts(
	val umkm_id: Int? = null,
	val type_name: String? = null,
	val updated_at: String? = null,
	val product_id: Int? = null,
	val link: Any? = null,
	val created_at: String? = null,
	val otp: Any? = null,
	val id: Int? = null,
	val status: String? = null
) : DataResponse<ResponseAssociatiedAccounts> {
	override fun retrieveData(): ResponseAssociatiedAccounts = this
}