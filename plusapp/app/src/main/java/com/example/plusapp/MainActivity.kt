package com.example.plusapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.my_nav_host_fragment)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> {
                    bottomNavigationView.visibility = View.GONE
                }
                R.id.registerFragment -> {
                    bottomNavigationView.visibility = View.GONE
                }
                R.id.drinksFragment -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
                R.id.eatsFragment -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
                R.id.settingsFragment -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }
}