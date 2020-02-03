package com.example.mooka_umkm.network

import com.example.mooka_umkm.Config
import com.example.mooka_umkm.network.lib.networkCall
import com.example.mooka_umkm.network.model.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object Repository {

    fun getAllUmkms() = networkCall<ListResponse<UMKM>, List<UMKM>> {
        client = ManagemenApi.apiService.getAllUmkms()
    }

    fun getUMKMId(id: Int) = networkCall<UMKM, UMKM> {
        client = ManagemenApi.apiService.getDetailUmkm(id)
    }

    fun getCommunities() = networkCall<ListResponse<Community>, List<Community>> {
        client = ManagemenApi.apiService.getCommunities()
    }

    fun postIkutCommunity(umkm_id: Int, communityId: String) = networkCall<CommunityUMKM, CommunityUMKM> {
        client = ManagemenApi.apiService.postIkutCommunity(umkm_id, communityId.toInt())
    }

    fun getAllUmkmCommunity(umkm_id: Int) = networkCall<ListResponse<CommunityUMKM>, List<CommunityUMKM>> {
        client = ManagemenApi.apiService.getAllUmkmCommunity(umkm_id)
    }

    fun getAllInbox(umkm_id: Int) = networkCall<ListResponse<Inbox>, List<Inbox>> {
        client = ManagemenApi.apiService.getAllInboxes(umkm_id)
    }

    fun addPoint (umkm_id: Int) = networkCall<UMKM, UMKM> {
        client = ManagemenApi.apiService.addCommunityPoint(umkm_id)
    }

    fun postNewProduct (
        umkmId:MultipartBody.Part,
        productName: MultipartBody.Part,
        productHarga: MultipartBody.Part,
        productStock: MultipartBody.Part,
        productDescription: MultipartBody.Part,
        file: MultipartBody.Part
    ) = networkCall<Product, Product> {
        client = ManagemenApi.apiService.postNewProduct(umkmId, productName, productHarga, productStock, productDescription, file)
    }

    fun getProductDetail (id:Int) = networkCall<Product, Product> { client = ManagemenApi.apiService.getProductDetail(id) }

    fun shareProduct (umkmId:Int,
                      productId:Int,
                      type_name:String,
                      username:String,
                      password:String) = networkCall<ResponseAssociatiedAccounts, ResponseAssociatiedAccounts> {
        client = ManagemenApi.apiService.shareProduct(umkmId, productId, type_name, username, password)
    }

    fun getStatusShare () = networkCall<ListResponse<ResponseAssociatiedAccounts>, List<ResponseAssociatiedAccounts>> {
        client = ManagemenApi.apiService.getShareStatus()
    }

    fun updateOTP (id: Int, otp:String) = networkCall<ResponseAssociatiedAccounts, ResponseAssociatiedAccounts> {
        client = ManagemenApi.apiService.updateOTP(id, otp)
    }
}

object ManagemenApi {
    var interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    var API_BASE_URL: String = Config.API_BASE_URL
    var httpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
    var builder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
    var retrofit = builder
        .client(httpClient.build())
        .build()

    var apiService: ApiService = retrofit.create<ApiService>(ApiService::class.java)
    val PREFNAME = "mooka"
}