package com.example.mooka_umkm.screens

import com.example.mooka_umkm.R


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.mooka_customer.extension.showmessage
import com.example.mooka_umkm.network.Repository
import com.example.mooka_umkm.network.lib.Resource
import com.example.mooka_umkm.network.model.MessageChat
import com.example.mooka_umkm.network.model.UMKM
import com.gemastik.raporsa.extension.setupNoAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_chatroom.*
import kotlinx.android.synthetic.main.fragment_chatroom.view.*
import kotlinx.android.synthetic.main.item_messages_send.view.*


/**
 * A simple [Fragment] subclass.
 */
class ChatroomFragment : Fragment() {

    var databaseReference: DatabaseReference? = null
    lateinit var umkm: UMKM
    var chatMessages = mutableListOf<MessageChat>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_chatroom, container, false)
        val chatroomFragmentArgs by navArgs<ChatroomFragmentArgs>()
        getUmkmDetail(view!!, chatroomFragmentArgs.umkmid)
        databaseReference = FirebaseDatabase.getInstance()
            .getReference("group_chat")
            .child("community-${chatroomFragmentArgs.communityid}")
            .child("messages")

        readMessage(chatroomFragmentArgs.umkmid)

        view.button_chatbox_send.setOnClickListener {
            val pesan = hashMapOf<String,Any>()
            pesan["isi"] = edittext_chatbox.text.toString()
            pesan["umkm"] = umkm
//            pesan["time"] = LocalDateTime.now()
            pesan["id"] = umkm.id
            pesan["nama_toko"] = umkm.nama
            pesan["nama_pemilik"] = umkm.nama_pemilik
            databaseReference!!.push().setValue(pesan)

            edittext_chatbox.onEditorAction(EditorInfo.IME_ACTION_DONE)
            edittext_chatbox.setText("")
        }
        return view
    }

    private fun readMessage(umkmid: Int) {
        databaseReference = FirebaseDatabase.getInstance()
            .getReference("group_chat")
            .child("community-$umkmid")
            .child("messages")

        databaseReference!!.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(ds: DataSnapshot) {
                chatMessages.clear()
                ds.children.forEach {
                    val messageChat = it.getValue(MessageChat::class.java)
                    chatMessages.add(messageChat!!)
                }
                rv_chat.setupNoAdapter(R.layout.item_messages_send,
                    chatMessages){v,m ->
                    v.text_message_name.text = m.nama_pemilik
                    v.text_message_body.text = m.isi
                }
                rv_chat.smoothScrollToPosition(chatMessages.count()-1);
            }

        })

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

