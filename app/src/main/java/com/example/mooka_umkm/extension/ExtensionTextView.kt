package com.example.mooka_umkm.extension

import android.content.Context
import android.widget.TextView

import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.example.mooka_umkm.R

fun String.boldFromHtml(): Spanned{
    return HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)
}

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInVisible() {
    visibility = View.INVISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

fun TextView.toA(){
    this.text = "A"
    this.background = ContextCompat.getDrawable(this.context, R.drawable.background_line_soft_circle_green)
}

fun TextView.toB(){
    this.text = "B"
    this.background = ContextCompat.getDrawable(this.context, R.drawable.background_line_soft_circle_lime)
}

internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

internal fun TextView.setTextColorRes(@ColorRes color: Int) = setTextColor(context.getColorCompat(color))