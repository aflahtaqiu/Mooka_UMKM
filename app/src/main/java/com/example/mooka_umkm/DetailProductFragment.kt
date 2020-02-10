package com.example.mooka_umkm

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.mooka_customer.extension.showmessage
import com.example.mooka_customer.extension.toRupiahs
import com.example.mooka_umkm.custom.DaysWeeksFormatter
import com.example.mooka_umkm.network.Repository
import com.example.mooka_umkm.network.lib.Resource
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail_product.view.*


/**
 * A simple [Fragment] subclass.
 */
class DetailProductFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R
            .layout.fragment_detail_product, container, false)

        val args by navArgs<DetailProductFragmentArgs>()

        Repository.getProductDetail(args.productid).observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{

                    Picasso.get().load(it.data?.gambar?.url).into(view.iv_banner)
                    view.tv_title.text = it.data?.title
                    view.tv_price.text = it.data?.harga.toString().toRupiahs()

                    view.tv_title_alamat.text = it.data?.umkm?.nama
                    view.tv_subtitle_alamat.text = it.data?.umkm?.alamat

                    view.tv_stok_barang.text = it.data?.stock.toString()
                    view.tv_berat_barang.text = it.data?.stock.toString()+ " gram"

                    Log.d("Success", it.data.toString())
                }
                Resource.ERROR ->{
                    Log.d("Error", it.message!!)
                    context?.showmessage("Something is wrong")
                }
            }
        })

        drawBarChart(view)

        return  view
    }

    private fun drawBarChart(view: View) {
        view.barchart.setDrawBarShadow(false)
        view.barchart.setDrawValueAboveBar(true)


        val xAxisFormatter = DaysWeeksFormatter()
        val xaxis = view.barchart.xAxis
        xaxis.position = XAxis.XAxisPosition.BOTTOM
        xaxis.setDrawGridLines(false)
        xaxis.granularity = 1f
        xaxis.labelCount = 7
        xaxis.valueFormatter = xAxisFormatter

        val barchartValue: ArrayList<BarEntry> = ArrayList()
        barchartValue.add(BarEntry(1.0f, 22.0f))
        barchartValue.add(BarEntry(2.0f, 41.0f))
        barchartValue.add(BarEntry(3.0f, 23.0f))
        barchartValue.add(BarEntry(4.0f, 50.0f))
        barchartValue.add(BarEntry(5.0f, 27.0f))
        barchartValue.add(BarEntry(6.0f, 51.0f))
        barchartValue.add(BarEntry(7.0f, 46.0f))

        val set1 = BarDataSet(barchartValue, "Jumlah Pengunjung Pada Produk Ini")

        set1.setDrawIcons(false)

        val dataSets: ArrayList<IBarDataSet> = ArrayList()
        dataSets.add(set1)

        val data = BarData(dataSets)
        data.setValueTextSize(10f)
        data.barWidth = 0.7f

        view.barchart.data = data
        view.barchart.description.isEnabled = false
    }


}
