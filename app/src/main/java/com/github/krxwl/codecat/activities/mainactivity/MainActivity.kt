package com.github.krxwl.codecat.activities.mainactivity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.krxwl.codecat.AssetDatabaseOpenHelper
import com.github.krxwl.codecat.BottomSheet
import com.github.krxwl.codecat.activities.loginactivity.LoginActivity
import com.github.krxwl.codecat.MyCourseFragment
import com.github.krxwl.codecat.R
import com.github.krxwl.codecat.activities.taskactivity.TaskActivity
import com.github.krxwl.codecat.databinding.BottomNavigationMenuBinding
import com.github.krxwl.codecat.entities.Course
import com.github.krxwl.codecat.entities.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val TAG = "MainActivity"
private const val KEY_SUBMODULE = "submodule"
class MainActivity : AppCompatActivity(), AllCoursesFragment.Callbacks, MyCourseFragment.Callbacks {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: BottomNavigationMenuBinding

    private val fragmentViewModel: FragmentViewModel by lazy {
        ViewModelProvider(this)[FragmentViewModel::class.java]
    }

    override fun onSubmoduleSelected(tasks: List<Task>) {
        val bundle = Bundle()
        bundle.putParcelableArrayList("tasks", tasks as java.util.ArrayList<out Parcelable>)
        var options = ActivityOptions.makeSceneTransitionAnimation(this,)
        startActivity(Intent(this, TaskActivity::class.java).putExtras(bundle).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY))
        finish()
    }

    override fun onCourseSelected(course: Course) {
        val bottomSheet = BottomSheet(course)
        bottomSheet.show(supportFragmentManager, BottomSheet.TAG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)

        // убирает задержку между активити
        setTheme(com.google.android.material.R.style.Theme_Material3_Dark_NoActionBar)
        super.onCreate(savedInstanceState)
        val adb = AssetDatabaseOpenHelper(this)
        adb.openDatabase()
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

        val currentFragment = supportFragmentManager.findFragmentById(R.id.bottom_navigation_frag)

        if (currentFragment == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.bottom_navigation_frag, fragmentViewModel.currentFragment).commit()
        }

        binding.bottomNavigation.setOnItemSelectedListener {
            item ->
            when(item.itemId) {
                R.id.item_my_course -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.bottom_navigation_frag, fragmentViewModel.myCourseFragment)
                        .commit()
                    true
                }
                R.id.item_all_couses -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.bottom_navigation_frag, fragmentViewModel.allCoursesFragment)
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

class FragmentViewModel: ViewModel() {
    var myCourseFragment = MyCourseFragment()
    var allCoursesFragment = AllCoursesFragment()
    var currentFragment: Fragment = myCourseFragment
}

