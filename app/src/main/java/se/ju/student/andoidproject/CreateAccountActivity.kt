package se.ju.student.andoidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import se.ju.student.andoidproject.databinding.ActivityMainBinding

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        supportActionBar?.hide()
        val createButton = findViewById<Button>(R.id.create_button)
        val validationErrorArray = mutableListOf<String>()


        createButton.setOnClickListener {
            val fullNameInput = findViewById<EditText>(R.id.full_name_input).editableText.toString()
            val emailInput = findViewById<EditText>(R.id.email_input).editableText.toString()
            val passwordInput = findViewById<EditText>(R.id.password_input).editableText.toString()
            val repeatPasswordInput = findViewById<EditText>(R.id.password_repeat_input).editableText.toString()
            when {
                fullNameInput.isNotEmpty() &&
                emailInput.isNotEmpty() &&
                passwordInput.isNotEmpty() &&
                repeatPasswordInput.isNotEmpty() -> {

                    Log.d("validation", "Nothing is Empty")
                    if(!fullNameInput.contains(" ")){
                        /* Add Validation Error To validationErrorArray*/
                        validationErrorArray.add("You must have a space in your full name")
                    }
                    if(!emailInput.contains("@")){
                        /* Add Validation Error To validationErrorArray*/
                        validationErrorArray.add("Email must to contain @ symbol")
                    }
                    if(passwordInput.length >= 6){
                        if(passwordInput != repeatPasswordInput){
                            /* Add Validation Error To validationErrorArray*/
                            validationErrorArray.add("Passwords do not match with each other ")
                        }
                    }else{
                        validationErrorArray.add("Password must be at least 6 characters")
                    }

                }
                else -> {
                    Log.d("validation", "Empty Fields")
                }
            }


            /*
            * ======================================
            * Add user info to Firebase
            * Send An Email To the User With a code.
            * ======================================
            *  */


            startActivity(
                Intent(this, VerifyAccountActivity::class.java)
            )
            finish()
        }

    }
}