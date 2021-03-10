package se.ju.student.andoidproject

import android.content.ClipData
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.view.View
import androidx.fragment.app.Fragment
import se.ju.student.andoidproject.databinding.ActivityHomePageBinding

class HomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        val binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.frame_layout, SettingFragment(), TAG_FRAGMENT_COUNT_UP)
                .add(R.id.frame_layout, NewestMemoryFragment(), TAG_FRAGMENT_HOME)
                .commitNow()
            changeToFragment(TAG_FRAGMENT_COUNT_UP)
        }


        binding.settingButton.setOnClickListener {
            changeToFragment(TAG_FRAGMENT_COUNT_UP)
        }

        binding.homeButton.setOnClickListener {
            changeToFragment(TAG_FRAGMENT_HOME)
        }

    }

    private fun changeToFragment(fragment_tag: String){

        with(supportFragmentManager.beginTransaction()){

            for(fragment in supportFragmentManager.fragments){
                hide(fragment)
            }

            show(supportFragmentManager.findFragmentByTag(fragment_tag)!!)

            commit()

        }


    }

    companion object {
        const val TAG_FRAGMENT_COUNT_UP = "TAG_FRAGMENT_COUNT_UP"
        const val TAG_FRAGMENT_HOME = "TAG_FRAGMENT_HOME"
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