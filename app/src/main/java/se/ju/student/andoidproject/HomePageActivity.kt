package se.ju.student.andoidproject

import android.content.ClipData
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        supportActionBar?.hide()

        val firstFragment = HomeFragment()
//        val secondFragment=AddFragment()
        val thirdFragment=SettingFragment()

        setCurrentFragment(firstFragment)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home_button->setCurrentFragment(firstFragment)
                R.id.add_button->startCreateMemoryActivity()
                R.id.settings_button->setCurrentFragment(thirdFragment)
            }
            true
        }

    }

    private fun startCreateMemoryActivity(){
        startActivity(Intent(this, CreateMemoryActivity::class.java))
        finish()
    }
    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

}

/*
* val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.btn1 -> {
                // put your code here
                return@OnNavigationItemSelectedListener true
            }
            R.id.btn2 -> {
                // put your code here
                return@OnNavigationItemSelectedListener true
            }
            R.id.btn3 -> {
                // put your code here
                return@OnNavigationItemSelectedListener true
            }
            R.id.btn4 -> {
                // put your code here
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

menuBar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

*
*
* */