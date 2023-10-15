package com.github.krxwl.codecat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.krxwl.codecat.databinding.BottomNavigationMenuBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), AllCoursesFragment.Callbacks {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: BottomNavigationMenuBinding

    override fun onCourseSelected(course: Course) {
        val bottomSheet = BottomSheet(course)
        bottomSheet.show(supportFragmentManager, BottomSheet.TAG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // убирает задержку между активити
        setTheme(com.google.android.material.R.style.Theme_Material3_Dark_NoActionBar)
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = BottomNavigationMenuBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val adb = AssetDatabaseOpenHelper(this)
        adb.openDatabase()

        // ДЛЯ ОТЛАДКИ
        //Firebase.auth.signOut()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val currentFragment = supportFragmentManager.findFragmentById(R.id.bottom_navigation_frag)

        if (currentFragment == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.bottom_navigation_frag, MyCourse()).commit()
        }

        binding.bottomNavigation.setOnItemSelectedListener {
            item ->
            when(item.itemId) {
                R.id.item_my_course -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.bottom_navigation_frag, MyCourse())
                        .commit()
                    true
                }
                R.id.item_all_couses -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.bottom_navigation_frag, AllCoursesFragment())
                        .commit()
                    true
                }
                R.id.item_profile -> {
                    true
                }
                else -> false
            }

        }

    }
}
