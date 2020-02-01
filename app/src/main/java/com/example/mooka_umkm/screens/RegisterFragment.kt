package com.example.mooka_umkm.screens


import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mooka_umkm.R
import kotlinx.android.synthetic.main.fragment_register.view.*
import java.text.DateFormatSymbols
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        view.btn_daftar.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToMainActivity())
        }
        val calendar = Calendar.getInstance()
        view.edittext.setOnClickListener {
            DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { _, year, month, day ->
//                tanggalSelesai = "$year-$month-$day"
                view.edittext.setText("$day ${DateFormatSymbols().months[month-1]} $year")
            },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE)).show()
        }
        spinnerConfig(view!!)
        view.tv_masuk_disini.setOnClickListener { findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())}
        return view
    }

    private fun spinnerConfig(view: View) {

    }
}
