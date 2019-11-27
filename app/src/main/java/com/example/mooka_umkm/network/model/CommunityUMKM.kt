package com.example.mooka_umkm.network.model

import com.example.mooka_umkm.network.lib.DataResponse

data class CommunityUMKM(
    val community_id: Int,
    val created_at: String,
    val id: Int,
    val is_admin: Boolean,
    val umkm_id: Int,
    val updated_at: String,
    val community: Community
) : DataResponse<CommunityUMKM> {
    override fun retrieveData(): CommunityUMKM = this
}