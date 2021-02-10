package se.ju.student.andoidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import se.ju.student.andoidproject.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val validationErrorArray = mutableListOf<String>() // To Add Any String to the array use --> validationErrorArray.add()

        binding.loginButton.setOnClickListener {

            val emailInput = binding.emailInput.editableText.toString()
            val passwordInput = binding.passwordInput.editableText.toString()


            /*
            * ============================
            * Check The Entered Value with the firebase value
            * if True  open HomePageActivity
            *  else show Validation Error
            * */

            startActivity(
                Intent(this, HomePageActivity::class.java)
            )
        }
    }
    /* Login with Facebook Account*/
    fun facebookLoginOnClick(view: View) {
        Log.d("facebook", "Hi From Facebook")
    }
    /* Login With Google Account*/
    fun googleLoginOnClick(view: View) {
        Log.d("google", "Hi From Google")
    }
    /* Change Language To English */
    fun englishLanguageOnClick(view: View) {}

    /* Change Language To Swedish */
    fun swedenLanguageOnClick(view: View) {}
    /*Open Create Account Activity*/
    fun toCreateAccountActivityOnClick(view: View) {
        val btn = findViewById<TextView>(R.id.new_user_text_view)

        btn.setOnClickListener {
            startActivity(
                Intent(this, CreateAccountActivity::class.java)
            )

        }


    }

    fun toForgetPasswordActivity(view: View) {}
}