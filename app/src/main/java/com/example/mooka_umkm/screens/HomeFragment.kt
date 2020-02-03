package com.example.mooka_umkm.screens


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mooka_customer.extension.showmessage
import com.example.mooka_customer.extension.toRupiahs
import com.example.mooka_umkm.R
import com.example.mooka_umkm.network.Repository
import com.example.mooka_umkm.network.lib.Resource
import com.gemastik.raporsa.extension.setupNoAdapter
import com.google.firebase.messaging.FirebaseMessaging
import com.pens.managementmasyrakat.extension.getPrefInt
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.item_produk_home.view.*

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

        val umkmId = context?.getPrefInt("umkm_id")

        view.tv_tambahkan_produk.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddProductFragment(umkmId!!))
        }

        FirebaseMessaging.getInstance().subscribeToTopic(umkmId.toString())
        return view
    }

    override fun onResume() {
        super.onResume()
        val umkm_id = context?.getPrefInt("umkm_id")
        getDetailUmkm(view!!, umkm_id!!)
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

                    view.tv_jumlah_produk.text = it.data!!.products.count().toString()
                    view.tv_produk_terbaik.text = it.data!!.products.firstOrNull()?.title


                    view.tv_pendapatan_per_bulan.text = "${it.data!!.orders.count()} Produk per Bulan"
                    val tot_price = it.data!!.orders.map { it.product }.sumBy { it.harga }
                    view.tv_penghasilan.text = "${tot_price.toString().toRupiahs()} per Bulan"

                    view.rv_all_products_home.setupNoAdapter(
                        R.layout.item_produk_home,
                        it.data!!.products,
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    ) {view, product ->
                        Picasso.get().load(product.gambar.url).into(view.iv_banner_product)
                        view.tv_name_product.text = product.title
                        view.tv_price_product.text = product.harga.toString().toRupiahs()
                        view.iv_share.setOnClickListener {
                            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToShareFragment(product.id, umkmId))
                        }
                    }

                    Log.d("Success", it.data.toString())

                }
                Resource.ERROR ->
                {
                    Log.d("Error", it.message!!)
                    context?.showmessage("Something is wrong")
                }
            }
        })
    }
}
