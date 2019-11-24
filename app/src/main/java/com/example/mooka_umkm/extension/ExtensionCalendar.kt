package com.gemastik.raporsa.extension

import java.text.SimpleDateFormat
import java.util.*

fun Calendar.getFormattedTanggal(): String{
    val tanggal = this[Calendar.DATE]
    val bulan = this.getNamaBulan()
    val tahun = this[Calendar.YEAR]
    return "$tanggal $bulan $tahun"
}

fun Calendar.getNamaTahun(): String{
    val yearDate = SimpleDateFormat("yyyy", Locale.getDefault())
    val year_name = yearDate.format(this.getTime())
    return year_name
}

fun Calendar.getNamaBulan(): String{
    val month_date = SimpleDateFormat("MMMM", Locale.getDefault())
    val month_name = month_date.format(this.getTime())
    return month_name

}