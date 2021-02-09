package se.ju.student.andoidproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

    }
    /* Login with Facebook Account*/
    fun facebookLoginOnClick(view: View) {
        Log.d("facebook", "Hi From Facebook")
    }

    /* Login With Google Account*/
    fun googleLoginOnClick(view: View) {
        Log.d("google", "Hi From Google")
    }
}