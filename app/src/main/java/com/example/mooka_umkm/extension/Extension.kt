package com.example.mooka_customer.extension

import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.text.InputType
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.mooka_umkm.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.threeten.bp.DayOfWeek
import org.threeten.bp.temporal.WeekFields
import java.text.SimpleDateFormat
import java.util.*


fun Context.showmessage(msg: String){
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}


fun Boolean.toInt() = if (this) 1 else 0

fun CheckBox.addEventDialogListener(update: (CheckBox) -> Unit){
    this.setOnClickListener {
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle("Perubahan data")
        builder.setMessage("Apakah anda yakin ingin merubah data")
        builder.setPositiveButton("YES"){ _, _ ->
            update(this)
        }

        builder.setNegativeButton("No"){ _, _ ->
            this.context.showmessage("Anda membatalkan")
            this.isChecked = !this.isChecked
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}

fun String.toRupiahs(): String{
    val money = String.format("%,d",this.toFloat().toInt())
    return "Rp, $money"
}

fun String.formatToDate(patternBefore: String, patternAfter: String): String {
    var spf = SimpleDateFormat(patternBefore, Locale.getDefault())
    val newDate = spf.parse(this)
    spf = SimpleDateFormat(patternAfter, Locale.getDefault())
    return spf.format(newDate!!)
}

fun View.addDialogDaftarArisanOnClick(judulArisan: String, update: () -> Unit){
    this.setOnClickListener {
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle("Mendaftar Arisan")
        builder.setMessage("Apakah anda yakin ingin mendaftar $judulArisan")
        builder.setPositiveButton("YES"){ _, _ ->
            this.context.showmessage("Permintaan Dikirim")
            update()
        }

        builder.setNegativeButton("No"){ _, _ ->
            this.context.showmessage("Permintaan Dibatalkan")
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}

//fun Context.showAlertDialog(
//    title: String = "Perubahan Data",
//    message: String = "Apakah anda yakin ingin melalukannya",
//    onYes: String = "Perubahan Data Berhasil",
//    onNo: String = "Perubahan Data Gagal",
//    onNegatifPressed: () -> Unit = {},
//    update: () ->
//){
//    val builder = AlertDialog.Builder(this)
//    builder.setTitle(title)
//    builder.setMessage(message)
//    builder.setPositiveButton("YES"){ _, _ ->
//        this.showmessage(onYes)
//        update()
//    }
//
//    builder.setNegativeButton("No"){ _, _ ->
//        onNegatifPressed()
//        this.showmessage(onNo)
//    }
//    val dialog: AlertDialog = builder.create()
//    dialog.show()
//}

fun Context.showAlertDialog(
    title: String = "Perubahan Data",
    message: String = "Apakah anda yakin ingin melalukannya"
){
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setMessage(message)
    val dialog: AlertDialog = builder.create()
    dialog.show()
}

fun Uri.getRealPath(context: Context): String? {
    var cursor: Cursor? = null
    try {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        cursor = context.contentResolver.query(this, proj, null, null, null);
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        return cursor?.getString(columnIndex!!)
    } finally {
        cursor?.close()
    }
}

fun View.toLoading() {
    if (this.tag == null){
        val progressBar = ProgressBar(this.context)
        this.tag = progressBar
        progressBar.isIndeterminate = true
        progressBar.visibility = View.VISIBLE
        val currentLayoutParams = this.layoutParams as ConstraintLayout.LayoutParams
        this.visibility = View.INVISIBLE
        (this.parent as ConstraintLayout).addView(progressBar, currentLayoutParams)
    }
}

fun View.finishLoading(){
    if (this.tag != null && this.tag is ProgressBar){
        val progressBar = this.tag as ProgressBar
        progressBar.visibility = View.GONE
        this.tag = null
        this.visibility = View.VISIBLE
    }
}

internal fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}


fun dpToPx(dp: Int, context: Context): Int =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
        context.resources.displayMetrics
    ).toInt()


fun daysOfWeekFromLocale(): Array<org.threeten.bp.DayOfWeek> {
    val firstDayOfWeek = org.threeten.bp.temporal.WeekFields.of(Locale.getDefault()).firstDayOfWeek
    var daysOfWeek = DayOfWeek.values()
    // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
    if (firstDayOfWeek != org.threeten.bp.DayOfWeek.MONDAY) {
        val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
        val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
        daysOfWeek = rhs + lhs
    }
    return daysOfWeek
}

fun ImageView.setDrawable(@DrawableRes id: Int){
    this.setImageDrawable(ContextCompat.getDrawable(this.context, id))
}

fun Context.hideKeyboardFrom(view: View) {
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

//fun Context.showEditableBottomSheetDialog(title: String = "Masukkan data baru",
//                                          text: String,
//                                          inputType: Int = InputType.TYPE_CLASS_TEXT,
//                                          update: (String) -> Unit){
//    val bottomSheetDialog = BottomSheetDialog(this)
//    val dialogView = LayoutInflater.from(this).inflate(R.layout.fragment_bottom_sheet_dialog, null)
//    dialogView.tv_batal.setOnClickListener {
//        bottomSheetDialog.dismiss()
//    }
//    bottomSheetDialog.setContentView(dialogView)
//    dialogView.tv_title.text = title
//    dialogView.et_data.setText(text)
//    dialogView.et_data.inputType= inputType
//    dialogView.tv_simpan.setOnClickListener {
//        update(dialogView.et_data.text.toString())
//        bottomSheetDialog.dismiss()
//    }
//    bottomSheetDialog.show()
//}