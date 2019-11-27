package com.example.mooka_umkm.screens


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.mooka_customer.extension.showmessage
import com.example.mooka_umkm.R
import com.example.mooka_umkm.network.Repository
import com.example.mooka_umkm.network.lib.Resource
import com.gemastik.raporsa.extension.setupNoAdapter
import com.pens.managementmasyrakat.extension.getPrefInt
import kotlinx.android.synthetic.main.fragment_inbox.view.*
import kotlinx.android.synthetic.main.item_inbox.view.*

/**
 * A simple [Fragment] subclass.
 */
class InboxFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_inbox, container, false)
        view.btn_kilik_disini.setOnClickListener{
            findNavController().navigate(InboxFragmentDirections.actionInboxFragmentToUmkmbisaFragment())
        }

        getAllInboxes(view!!)


        return  view
    }

    private fun getAllInboxes(view: View) {
        val umkm_id = context?.getPrefInt("umkm_id")
        Repository.getAllInbox(umkm_id!!).observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    view.rv_inbox.setupNoAdapter(
                        R.layout.item_inbox,
                        it.data!!
                        ){view,inbox ->
                        view.tv_judul_inbox.text = inbox.judul
                        view.tv_deskripsi_inbox.text = inbox.text
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
