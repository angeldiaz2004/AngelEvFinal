package com.ang.ec4angel.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ang.ec4angel.*
import com.ang.ec4angel.databinding.ActivityMainBinding
import com.ang.ec4angel.views.fragment.ApiFragment
import com.ang.ec4angel.views.fragment.ConfigFragment
import com.ang.ec4angel.views.fragment.FavoriteFragment
import com.ang.ec4angel.views.fragment.FirebaseFragment
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)
        fAuth = Firebase.auth

        accederFragmento(ConfigFragment())

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.api -> accederFragmento(ApiFragment())
                R.id.favoritos -> accederFragmento(FavoriteFragment())
                R.id.firebase -> accederFragmento(FirebaseFragment())
                R.id.config -> accederFragmento(ConfigFragment())

                else -> {

                }
            }
            true
        }
    }

    private fun accederFragmento(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}