package com.example.mooka_umkm.screens


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.mooka_customer.extension.showmessage
import com.example.mooka_umkm.R
import com.example.mooka_umkm.network.Repository
import com.example.mooka_umkm.network.lib.Resource
import com.example.mooka_umkm.network.model.UMKM
import com.gemastik.raporsa.extension.setupNoAdapter
import kotlinx.android.synthetic.main.fragment_umkmbisa.view.*
import kotlinx.android.synthetic.main.item_umkmbisa.view.*

/**
 * A simple [Fragment] subclass.
 */
class UmkmbisaFragment : Fragment() {

    var idDrawables = arrayOf(R.drawable.avatar_1, R.drawable.avatar_2, R.drawable.avatar_3)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_umkmbisa, container, false)
        setupUmkmbisaRecyclerView(view!!)
        return view
    }


    private fun setupUmkmbisaRecyclerView(view: View) {
        Repository.getAllUmkms().observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    val data: List<UMKM> = it.data!!.sortedByDescending { umkm -> umkm.point }.take(3)
                    view.rv_umkmbisa.setupNoAdapter(
                        R.layout.item_umkmbisa,
                        data
                    ) {view, umkm ->
                        view.iv_avatar_umkm.setImageDrawable(ContextCompat.getDrawable(context!!, idDrawables[umkm.id % 3]))
                        view.tv_nama_toko.text =  umkm.nama + " (" + umkm.point + " poin )"
                    }
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
