package com.example.mooka_umkm.screens


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mooka_customer.extension.showAlertDialog
import com.example.mooka_customer.extension.showmessage
import com.example.mooka_umkm.R
import com.example.mooka_umkm.network.Repository
import com.example.mooka_umkm.network.lib.Resource
import com.example.mooka_umkm.network.model.Community
import com.example.mooka_umkm.network.model.CommunityUMKM
import com.example.mooka_umkm.services.NotificationService
import com.gemastik.raporsa.extension.setupNoAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.pens.managementmasyrakat.extension.getPrefInt
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_community.view.*
import kotlinx.android.synthetic.main.item_komunitas_saya.view.*
import java.util.HashMap

/**
 * A simple [Fragment] subclass.
 */
class CommunityFragment : Fragment() {

    var umkmId: Int? = -1
    var reference:DatabaseReference? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_community, container, false)
        umkmId = context?.getPrefInt("umkm_id")
        setupRekomendasi(view!!, umkmId)
        setupKomunitasSaya(view, umkmId)
        setupKomunitasKota(view, umkmId)

        view.mtb_1.setOnClickListener {
//                        val umkmId = getPrefInt("umkm_id")
            NotificationService.getInstance(context).sendNotifToUmkm("all", "Baju Kuning","Barang Terjual Murah")
        }
        return view
    }

    private fun setupKomunitasSaya(view: View, umkmId: Int?) {
        Repository.getAllUmkmCommunity(umkmId!!).observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    val data: List<CommunityUMKM> = it.data!!
//                        .map { communityUMKM -> communityUMKM.community }
                        .sortedByDescending { community -> community.community.official }
                    view.rv_komunitas_saya.setupNoAdapter(
                        R.layout.item_komunitas_saya,
                        data,
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false),
                        ::bindKomunitasSaya
                    )
                    Log.d("Success", it.data.toString())
                }
                Resource.ERROR ->{
                    Log.d("Error", it.message!!)
                    context?.showmessage("Something is wrong")
                }
            }
        })
    }

    private fun setupRekomendasi(view: View, umkmId: Int?) {
        Repository.getCommunities().observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    val data = it.data!!.sortedByDescending { community -> community.official }
                    view.rv_rekomendasi_komunitas.setupNoAdapter(
                        R.layout.item_komunitas_saya,
                        data,
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false),
                        ::bindRekomendasi
                    )
                    Log.d("Success", it.data.toString())
                }
                Resource.ERROR ->{
                    Log.d("Error", it.message!!)
                    context?.showmessage("Something is wrong")
                }
            }
        })
    }

    private fun setupKomunitasKota (view: View, umkmId: Int?) {
        Repository.getCommunities().observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    val data = it.data!!.sortedByDescending { community -> community.official }
                        .filter { it.title.contains("Kota") }
                    view.rv_komunitas_kota.setupNoAdapter(
                        R.layout.item_komunitas_saya,
                        data,
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false),
                        ::bindKomunitasKota
                    )
                    Log.d("Success", it.data.toString())
                }
                Resource.ERROR ->{
                    Log.d("Error", it.message!!)
                    context?.showmessage("Something is wrong")
                }
            }
        })
    }

    fun bindKomunitasKota (view: View, community: Community) {
        Picasso.get().load(community.banner.url).into(view.iv_banner)
        view.tv_title.text = community.title
        view.tv_subtitle.text = community.subtitle
        if (!community.official)
            view.iv_checked.visibility = View.GONE
        view.setOnClickListener {
            context?.showAlertDialog("Join Komunitas","Apakah anda ingin masuk forum \" ${community.title} \" ini ?"
                ,"", ""){
                tambahKomunitas(community.id)
            }
        }
    }

    fun bindRekomendasi(view: View, community: Community) {
        Picasso.get().load(community.banner.url).into(view.iv_banner)
        view.tv_title.text = community.title
        view.tv_subtitle.text = community.subtitle
        if (!community.official)
            view.iv_checked.visibility = View.GONE
        view.setOnClickListener {
            context?.showAlertDialog("Join Komunitas","Apakah anda ingin masuk forum \" ${community.title} \" ini ?"
            ,"", ""){
                tambahKomunitas(community.id)
            }
        }
    }

    private fun tambahKomunitas(id: Int) {
        Repository.postIkutCommunity(umkmId!!, id.toString()).observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    context?.showmessage("Daftar Komunitas Berhasil")
                    addSayaKeKomunitasFirebase(id, umkmId!!)
                    setupKomunitasSaya(view!!, umkmId)
                    Log.d("Success", it.data.toString())
                }
                Resource.ERROR ->{
                    Log.d("Error", it.message!!)
                    context?.showmessage("Komunitas Sudah Ditambahkan")
                }
            }
        })
    }

    private fun addSayaKeKomunitasFirebase(id: Int, umkmId: Int) {
        reference = FirebaseDatabase.getInstance().getReference("group_chat")
            .child("community-$id")
            .child("anggota")
        val user = HashMap<String,String>()


        user[umkmId.toString()] = "asd"
        reference!!.push().setValue(user)
    }

    fun bindKomunitasSaya(view: View, communityUMKM: CommunityUMKM) {
        Picasso.get().load(communityUMKM.community.banner.url).into(view.iv_banner)
        view.tv_title.text = communityUMKM.community.title
        view.tv_subtitle.text = communityUMKM.community.subtitle
        if (!communityUMKM.community.official)
            view.iv_checked.visibility = View.GONE
        view.setOnClickListener {
            findNavController().navigate(CommunityFragmentDirections.actionCommunityFragmentToChatroomFragment(communityUMKM.community.id, umkmId!!, communityUMKM.is_admin))
        }
    }
}

