package com.github.krxwl.codecat

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.github.krxwl.codecat.databinding.BottomNavigationMenuBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: BottomNavigationMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // убирает задержку между активити
        setTheme(com.google.android.material.R.style.Theme_Material3_Dark_NoActionBar)
        super.onCreate(savedInstanceState)


        auth = Firebase.auth
        binding = BottomNavigationMenuBinding.inflate(layoutInflater)

        setContentView(binding.root)
        // ДЛЯ ОТЛАДКИ
        //Firebase.auth.signOut()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        /*NavigationBarView.OnItemReselectedListener {
            item ->
            when(item.itemId) {
                R.id.item_my_course -> {
                    true
                }
                R.id.item_all_couses -> {
                    true
                }
                R.id.item_profile -> {
                    true
                }
                else -> false
            }


        }
    */
    }
}
