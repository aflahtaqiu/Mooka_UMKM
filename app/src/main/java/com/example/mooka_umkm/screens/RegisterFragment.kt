package com.example.mooka_umkm.screens


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mooka_umkm.R
import kotlinx.android.synthetic.main.fragment_register.view.*

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
        view.tv_masuk_disini.setOnClickListener { findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())}
        return view
    }
}
