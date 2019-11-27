package com.example.mooka_umkm.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.mooka_customer.extension.showAlertDialog
import com.example.mooka_customer.extension.showmessage
import com.example.mooka_umkm.R
import com.example.mooka_umkm.network.Repository
import com.example.mooka_umkm.network.lib.Resource
import com.example.mooka_umkm.network.model.UMKM
import com.pens.managementmasyrakat.extension.savePref
import kotlinx.android.synthetic.main.fragment_login.view.*

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        view.tv_daftar_disini.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
        view.btn_login.setOnClickListener {
            loginUmkm(view.et_no_telp.text.toString(), view.et_password.text.toString())
        }
        return view
    }

    private fun loginUmkm(noTelp:String, password:String) {
        Repository.getAllUmkms().observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    val umkm:UMKM? = it.data!!.find { umkm ->  noTelp == umkm.phone && password == umkm.password }
                    if (umkm == null)
                        context!!.showAlertDialog("Gagal Login!", "Pastikan nomor telepon dan password Anda telah terdaftar")
                    else {
                        context!!.savePref("umkm_id", umkm.id)
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainActivity())
                    }
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
