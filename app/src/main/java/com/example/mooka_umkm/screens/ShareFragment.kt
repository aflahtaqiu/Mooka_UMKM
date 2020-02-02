package com.example.mooka_umkm.screens


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.mooka_umkm.R

/**
 * A simple [Fragment] subclass.
 */
class ShareFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val  view = inflater.inflate(R.layout.fragment_share, container, false)

        val shareFragmentArgs by navArgs<ShareFragmentArgs>()
        return view
    }
}
