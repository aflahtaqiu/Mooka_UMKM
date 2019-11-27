package com.example.mooka_umkm.screens


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.mooka_customer.extension.showmessage
import com.example.mooka_umkm.R
import com.example.mooka_umkm.network.Repository
import com.example.mooka_umkm.network.lib.Resource
import com.example.mooka_umkm.network.model.UMKM
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_chatroom.*
import kotlinx.android.synthetic.main.fragment_chatroom.view.*
import org.threeten.bp.LocalDateTime
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ChatroomFragment : Fragment() {

    var databaseReference: DatabaseReference? = null
    lateinit var umkm: UMKM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_chatroom, container, false)
        val chatroomFragmentArgs by navArgs<ChatroomFragmentArgs>()
        getUmkmDetail(view!!, chatroomFragmentArgs.umkmid);
        databaseReference = FirebaseDatabase.getInstance().getReference("group_chat")
            .child("community-${chatroomFragmentArgs.communityid}")
            .child("messages")
        view.button_chatbox_send.setOnClickListener {
            val pesan = hashMapOf<String,Any>()
            pesan["isi"] = edittext_chatbox.text.toString()
            pesan["umkm"] = umkm
//            pesan["time"] = LocalDateTime.now()
            pesan["id"] = umkm.id
            pesan["nama_toko"] = umkm.nama
            pesan["nama_pemilik"] = umkm.nama_pemilik
            databaseReference!!.push().setValue(pesan)
        }
        return view
    }

    private fun getUmkmDetail(view: View, umkm: Int) {
        Repository.getUMKMId(umkm).observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    this.umkm = it.data!!
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
