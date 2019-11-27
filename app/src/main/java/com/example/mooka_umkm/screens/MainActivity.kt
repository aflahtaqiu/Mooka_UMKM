package com.example.mooka_umkm.screens

import com.example.mooka_umkm.R

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mooka_umkm.services.NotificationService
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.pens.managementmasyrakat.extension.getPrefInt
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.fragment)
        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, bundle: Bundle? ->
            supportActionBar?.title = nd.label

            when(nd.id) {
                R.id.chatroomFragment-> hideBottomNavigation()
                else -> showBottomNavigation()
            }
        }
        val umkmId = getPrefInt("umkm_id")
        FirebaseMessaging.getInstance().subscribeToTopic("all")

        navigation.setupWithNavController(navController )

        // Write a message to the database
    }

    private fun hideBottomNavigation() {

        // bottom_navigation is BottomNavigationView
        with(navigation) {
            if (visibility == View.VISIBLE && alpha == 1f) {
                animate()
                    .alpha(0f)
                    .withEndAction { visibility = View.GONE }
                    .duration = 500
            }
        }
    }

    private fun showBottomNavigation() {
        // navigation is BottomNavigationView
        with(navigation) {
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .duration = 500
        }
    }
}
