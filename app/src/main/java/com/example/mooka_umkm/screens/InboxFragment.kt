package com.example.mooka_umkm.screens


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mooka_umkm.R

/**
 * A simple [Fragment] subclass.
 */
class InboxFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_inbox, container, false)
        view.setOnClickListener{
            findNavController().navigate(InboxFragmentDirections.actionInboxFragmentToUmkmbisaFragment())
        }

        return  view
    }


}
