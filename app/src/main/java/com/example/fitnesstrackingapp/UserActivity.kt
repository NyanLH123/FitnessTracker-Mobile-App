package com.example.fitnesstrackingapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fitnesstrackingapp.databinding.ActivityUserBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.userFragmentContainer) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)

        val data = intent.extras
        val id = data?.getInt("id")
        val username = data?.getInt("username")
        Log.d("UserActivity","***$id $username")
        navController.navigate(R.id.userHomeFragment, data)

        binding.bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_home -> {
                    navController.navigate(R.id.userHomeFragment, data)
                    true
                }
                R.id.item_add -> {
                    navController.navigate(R.id.entryFragment, data)
                    true
                }
                R.id.item_list -> {
                    navController.navigate(R.id.listFragment, data)
                    true
                }
                R.id.item_profile -> {
                    navController.navigate(R.id.userHomeFragment, data)
                    true
                } else -> false
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val result = when(item.itemId) {
            R.id.item_logout->{
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else-> false
        }
        return result
    }
}