package com.example.mooka_umkm.network

import com.example.mooka_umkm.network.model.*
import com.google.gson.JsonObject
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("umkms")
    fun getAllUmkms() : Deferred<Response<ListResponse<UMKM>>>

    @GET("umkms/{umkm_id}")
    fun getDetailUmkm(
        @Path("umkm_id") id: Int
    ) : Deferred<Response<UMKM>>

    @GET("communities")
    fun getCommunities() : Deferred<Response<ListResponse<Community>>>

    @FormUrlEncoded
    @POST("umkms/{umkm_id}/community_umkms")
    fun postIkutCommunity(
        @Path("umkm_id") id: Int,
        @Field("community_id") communityId: Int
    ) : Deferred<Response<CommunityUMKM>>

    @GET("umkms/{umkm_id}/community_umkms")
    fun getAllUmkmCommunity(
        @Path("umkm_id") id: Int
    ) : Deferred<Response<ListResponse<CommunityUMKM>>>

    @POST("umkms/{umkm_id}/community_umkms/add_point")
    fun addCommunityPoint (
        @Path("umkm_id") id: Int
    ) : Deferred<Response<UMKM>>

    @GET("umkms/{umkm_id}/inboxes")
    fun  getAllInboxes(
        @Path("umkm_id") id: Int
    ) : Deferred<Response<ListResponse<Inbox>>>

    @Multipart
    @POST("products")
    fun postNewProduct (
        @Part umkmId: MultipartBody.Part,
        @Part productName: MultipartBody.Part,
        @Part productHarga: MultipartBody.Part,
        @Part productStock: MultipartBody.Part,
        @Part productDescription: MultipartBody.Part,
        @Part file: MultipartBody.Part
    ) :Deferred<Response<Product>>

    @GET("products/{product_id}")
    fun getProductDetail(
        @Path("product_id") id: Int
    ): Deferred<Response<Product>>

    @FormUrlEncoded
    @POST("associatied_accounts")
    fun shareProduct(
        @Field("umkm_id") umkmId:Int,
        @Field("product_id") productId:Int,
        @Field("type_name")typeName:String,
        @Field("username") username:String,
        @Field("password") password:String
    ):Deferred<Response<ResponseAssociatiedAccounts>>

    @GET("associatied_accounts")
    fun getShareStatus (): Deferred<Response<ListResponse<ResponseAssociatiedAccounts>>>

    @FormUrlEncoded
    @PUT("associatied_accounts/{id}")
    fun updateOTP(
        @Path("id") id: Int,
        @Field("otp") otp:String
    ) : Deferred<Response<ResponseAssociatiedAccounts>>
}