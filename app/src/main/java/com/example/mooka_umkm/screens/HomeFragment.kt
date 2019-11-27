package com.example.mooka_umkm.screens


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.mooka_customer.extension.showmessage
import com.example.mooka_umkm.R
import com.example.mooka_umkm.network.Repository
import com.example.mooka_umkm.network.lib.Resource
import com.google.firebase.messaging.FirebaseMessaging
import com.pens.managementmasyrakat.extension.getPrefInt
import kotlinx.android.synthetic.main.fragment_home.view.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        FirebaseMessaging.getInstance().subscribeToTopic("pengunguman")
        val umkm_id = context?.getPrefInt("umkm_id")
        getDetailUmkm(view!!, umkm_id!!)
        return view
    }

    private fun getDetailUmkm(view: View, umkmId: Int) {
        Repository.getUMKMId(umkmId).observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    view.tv_nama.text = it.data!!.nama
                    view.tv_phone.text = it.data!!.phone
                    view.tv_nama_pemilik.text = it.data!!.nama_pemilik

                    view.tv_rating.text = "5.0 / 5"
                    view.tv_bulan_bergabung.text = "0 Bulan Lalu"

                    view.tv_jumlah_produk.text = it.data!!.orders.count().toString()
                    view.tv_produk_terbaik.text = it.data!!.products.firstOrNull()?.title

                    Log.d("Success", it.data.toString())
                }
                Resource.ERROR ->{
                    Log.d("Error", it.message!!)
                    context?.showmessage("Something is wrong")
                }
            }
        })
    }
}
