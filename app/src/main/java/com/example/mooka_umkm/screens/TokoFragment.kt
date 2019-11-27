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
import com.pens.managementmasyrakat.extension.getPrefInt
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_toko.view.*

/**
 * A simple [Fragment] subclass.
 */
class TokoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_toko, container, false)

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
                    view.tv_nama_toko.text = it.data!!.nama
                    view.tv_email_toko.text = it.data!!.email

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
