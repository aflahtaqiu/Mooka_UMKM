package com.gemastik.raporsa.extension

import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mooka_umkm.adapter.BaseAdapter

@Suppress("UNCHECKED_CAST")
fun <T> RecyclerView.setupNoAdapter(layout: Int,
                                      listItem: List<T> = ArrayList(),
                                    layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context),
                                      bindView: (View, T) -> Unit) {
    this.layoutManager = layoutManager
    val adapter = BaseAdapter(layout,bindView)
    this.adapter = adapter
    (this.adapter as BaseAdapter<T>).swapData(listItem)
    this.adapter?.notifyDataSetChanged()
}

@Suppress("UNCHECKED_CAST")
fun <T> RecyclerView.refreshNoAdapterRecyclerView(listItem: List<T> = ArrayList()) {
    this.adapter?.let {
        (it as BaseAdapter<T>).swapData(listItem)
        it.notifyDataSetChanged()
    }
}

fun RecyclerView.addDecoration() {
    val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
    this.addItemDecoration(dividerItemDecoration)
}