package com.example.mooka_umkm.screens


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.mooka_customer.extension.showAlertDialog
import com.example.mooka_customer.extension.showEditableBottomSheetDialog
import com.example.mooka_customer.extension.showmessage
import com.example.mooka_customer.extension.toRupiahs
import com.example.mooka_umkm.R
import com.example.mooka_umkm.network.Repository
import com.example.mooka_umkm.network.lib.Resource
import com.example.mooka_umkm.network.model.TYPEDNAME
import com.pens.managementmasyrakat.extension.getPrefString
import com.pens.managementmasyrakat.extension.savePref
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_share.*
import kotlinx.android.synthetic.main.fragment_share.view.*

/**
 * A simple [Fragment] subclass.
 */
class ShareFragment : Fragment() {

    val successStatus = "SUCCESS"
    val requestStatus = "REQUESTING"
    val gagalStatus = "GAGAL"

    private var idPost:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val  view = inflater.inflate(R.layout.fragment_share, container, false)

        val shareFragmentArgs by navArgs<ShareFragmentArgs>()

        Repository.getProductDetail(shareFragmentArgs.productid).observe(this, Observer {
            when(it?.status){
            Resource.LOADING ->{
                Log.d("Loading", it.status.toString())
            }
            Resource.SUCCESS ->{
                Log.e("Success detailproduct", it.data.toString())

                Picasso.get().load(it.data!!.gambar.url).into(view.imageView8)
                view.tv_nama_barang.text = it.data!!.title
                view.tv_harga_barang.text = it.data!!.harga.toString().toRupiahs()
                view.tv_stok_barang.text = it.data!!.stock.toString()
                view.tv_berat_barang.text = it.data!!.berat.toString()+" Gram"
                view.tv_deskripsi_barang.text = it.data!!.description
            }
            Resource.ERROR ->{
                Log.d("Error", it.message!!)
                context?.showmessage("Something is wrong")
            }
        } })

        view.btn_upload_facebook.setOnClickListener {
            if (context!!.getPrefString("usernameFacebook").isNullOrEmpty()) {
                context!!.showEditableBottomSheetDialog{username, password ->
                    context!!.savePref("usernameFacebook", username)
                    context!!.savePref("passwordFacebook", password)
                }
            } else
                shareProduct(shareFragmentArgs.umkmid,
                    shareFragmentArgs.productid,
                    TYPEDNAME.FACEBOOK.name,
                    context!!.getPrefString("usernameFacebook")!!,
                    context!!.getPrefString("passwordFacebook")!!)
        }

        view.btn_upload_shopee.setOnClickListener {
            if (context!!.getPrefString("usernameShopee").isNullOrEmpty()) {
                context!!.showEditableBottomSheetDialog {username, password ->
                    context!!.savePref("usernameShopee", username)
                    context!!.savePref("passwordShopee", password)
                }
            } else
                shareProduct(shareFragmentArgs.umkmid,
                    shareFragmentArgs.productid,
                    TYPEDNAME.SHOPEE.name,
                    context!!.getPrefString("usernameShopee")!!,
                    context!!.getPrefString("passwordShopee")!!)
        }

        view.btn_upload_tokped.setOnClickListener {
            if (context!!.getPrefString("usernameTokped").isNullOrEmpty()) {
                context!!.showEditableBottomSheetDialog{username, password ->
                    context!!.savePref("usernameTokped", username)
                    context!!.savePref("passwordTokped", password)
                }
            } else {
                shareProduct(shareFragmentArgs.umkmid,
                    shareFragmentArgs.productid,
                    TYPEDNAME.TOKPED.name,
                    context!!.getPrefString("usernameTokped")!!,
                    context!!.getPrefString("passwordTokped")!!)

                view.textInputLayout11.visibility = View.VISIBLE
                view.et_otp.visibility = View.VISIBLE
                view.btn_kirim_otp.visibility = View.VISIBLE

                view.btn_kirim_otp.setOnClickListener {
                    if(idPost!=0) {
                        Repository.updateOTP(idPost, view.et_otp.text.toString()).observe(this, Observer {
                            when(it?.status){
                                Resource.LOADING ->{
                                    Log.d("Loading", it.status.toString())
                                }
                                Resource.SUCCESS ->{
                                    Log.d("Success", it.data.toString())
                                    context!!.showAlertDialog("OTP", "Selamat, OTP Anda berhasil dikirim")
                                }
                                Resource.ERROR ->{
                                    Log.d("Error", it.message!!)
                                    context?.showmessage("Something is wrong")
                                }
                            }
                        })
                    }
                }
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        val shareFragmentArgs by navArgs<ShareFragmentArgs>()
        getStatusShare(shareFragmentArgs.productid)
    }

    private fun shareProduct (umkmId:Int, productId: Int, typedName:String, username:String, password: String) {
        Repository.shareProduct(umkmId, productId, typedName, username, password)
            .observe(this, Observer {
                when(it?.status){
                    Resource.LOADING ->{
                        Log.d("Loading", it.status.toString())
                    }
                    Resource.SUCCESS ->{
                        Log.d("Success", it.data.toString())
                        idPost = it.data!!.id!!
                        getStatusShare(it.data!!.product_id!!)
                    }
                    Resource.ERROR ->{
                        Log.d("Error", it.message!!)
                        context?.showmessage("Something is wrong")
                    }
                }
            })
    }

    private fun getStatusShare (productId:Int) {
        Repository.getStatusShare().observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    Log.d("Success", it.data.toString())
                    it.data!!.forEach {
                        if (it.product_id == productId ) {
                            idPost = it.id!!
                            when(it.status) {
                                successStatus -> {
                                    when(it.type_name) {
                                        TYPEDNAME.SHOPEE.name -> {
                                            tv_status_shopee.text = "Sukses"
                                            tv_status_shopee.setTextColor(activity!!.resources.getColor(R.color.green_500))
                                        }
                                        TYPEDNAME.FACEBOOK.name -> {
                                            tv_status_facebook.text = "Sukses"
                                            tv_status_facebook.setTextColor(activity!!.resources.getColor(R.color.green_500))
                                        }
                                        TYPEDNAME.TOKPED.name -> {
                                            tv_status_tokopedia.text = "Sukses"
                                            tv_status_tokopedia.setTextColor(activity!!.resources.getColor(R.color.green_500))
                                        }
                                    }
                                }
                                requestStatus -> {
                                    when(it.type_name) {
                                        TYPEDNAME.SHOPEE.name -> {
                                            tv_status_shopee.text = "Requesting..."
                                            tv_status_shopee.setTextColor(activity!!.resources.getColor(R.color.blue_500))
                                        }
                                        TYPEDNAME.FACEBOOK.name -> {
                                            tv_status_facebook.text = "Requesting..."
                                            tv_status_facebook.setTextColor(activity!!.resources.getColor(R.color.blue_500))
                                        }
                                        TYPEDNAME.TOKPED.name -> {
                                            tv_status_tokopedia.text = "Requesting..."
                                            tv_status_tokopedia.setTextColor(activity!!.resources.getColor(R.color.blue_500))
                                        }
                                    }
                                }
                                gagalStatus -> {
                                    when(it.type_name) {
                                        TYPEDNAME.SHOPEE.name -> {
                                            tv_status_shopee.text = "Gagal"
                                            tv_status_shopee.setTextColor(activity!!.resources.getColor(R.color.red_500))
                                        }
                                        TYPEDNAME.FACEBOOK.name -> {
                                            tv_status_facebook.text = "Gagal"
                                            tv_status_facebook.setTextColor(activity!!.resources.getColor(R.color.red_500))
                                        }
                                        TYPEDNAME.TOKPED.name -> {
                                            tv_status_tokopedia.text = "Gagal"
                                            tv_status_tokopedia.setTextColor(activity!!.resources.getColor(R.color.red_500))
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
                Resource.ERROR ->{
                    Log.d("Error", it.message!!)
                    context?.showmessage("Something is wrong")
                }
            }
        })
    }
}
