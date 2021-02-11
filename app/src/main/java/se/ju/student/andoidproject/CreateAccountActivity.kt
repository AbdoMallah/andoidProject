package se.ju.student.andoidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import se.ju.student.andoidproject.databinding.ActivityMainBinding

const val MINIMUM_FIRSTNAME_CHARACTERS = 2
const val MAXIMUM_FIRSTNAME_CHARACTERS = 20
const val MINIMUM_LASTNAME_CHARACTERS = 2
const val MAXIMUM_LASTNAME_CHARACTERS = 20
const val MINIMUM_PASSWORD_CHARACTERS = 8
const val MAXIMUM_PASSWORD_CHARACTERS = 50

class CreateAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        supportActionBar?.hide()
        val createButton = findViewById<Button>(R.id.create_button)
        val validationErrorArray = mutableListOf<String>()
        //val minFirstNameChararchets = 2
        if( checkEmailIsValid("example@studnet.ju.com")){
            Log.d("Nice", "Works")
        }else{
            Log.d("bad", "don't work")
        }

        createButton.setOnClickListener {
            val firstNameInput = findViewById<EditText>(R.id.first_name_input).editableText.toString()
            val lastNameInput = findViewById<EditText>(R.id.last_name_input).editableText.toString()
            val emailInput = findViewById<EditText>(R.id.email_input).editableText.toString()
            val passwordInput = findViewById<EditText>(R.id.password_input).editableText.toString()
            val repeatPasswordInput = findViewById<EditText>(R.id.password_repeat_input).editableText.toString()
            when {
                firstNameInput.isNotEmpty() &&
                lastNameInput.isNotEmpty() &&
                emailInput.isNotEmpty() &&
                passwordInput.isNotEmpty() &&
                repeatPasswordInput.isNotEmpty() -> {
                    if(firstNameInput.length < MINIMUM_FIRSTNAME_CHARACTERS){
                        validationErrorArray.add("Your first name is to short ("+ MINIMUM_FIRSTNAME_CHARACTERS +"characters at least)")
                    }
                    if(firstNameInput.length > MAXIMUM_FIRSTNAME_CHARACTERS){
                        validationErrorArray.add("Your first name is to LONG ("+ MAXIMUM_FIRSTNAME_CHARACTERS +"characters max)")
                    }
                    if(firstNameInput.length < MINIMUM_LASTNAME_CHARACTERS){
                        validationErrorArray.add("Your last name is to short ("+ MINIMUM_LASTNAME_CHARACTERS +"characters at least)")
                    }
                    if(firstNameInput.length > MAXIMUM_LASTNAME_CHARACTERS){
                        validationErrorArray.add("Your last name is to LONG ("+ MAXIMUM_LASTNAME_CHARACTERS +"characters max)")
                    }
                    if(!emailInput.contains("@")){
                        /* Add Validation Error To validationErrorArray*/
                        validationErrorArray.add("Email must to contain @ symbol")
                    }
                    if(passwordInput.length >= MINIMUM_PASSWORD_CHARACTERS){
                        if(passwordInput != repeatPasswordInput){
                            /* Add Validation Error To validationErrorArray*/
                            validationErrorArray.add("Passwords do not match with each other ")
                        }
                    }else{
                        validationErrorArray.add("Password must be at least 6 characters")
                    }

                }
                else -> {
                    Log.d("validation", resources.getString(R.string.empty_field))
                }
            }
            for (error in validationErrorArray){
                Log.d("validation", error)
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

    /*
    * Check email is valid
    * Boolean : True if email is valid and false if not
    * checkEmailIsValid(string)
    * */
    private fun checkEmailIsValid(enteredEmail: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(enteredEmail).matches()
    }
    private fun checkPassword(enteredPassword: EditText, enteredNewPassword:EditText): Boolean {
        val password = enteredPassword.editableText.toString()
        val newPass = enteredNewPassword.editableText.toString()
        when {
            password.isNotEmpty() && newPass.isNotEmpty() -> {
                if(password.length >= MINIMUM_PASSWORD_CHARACTERS){
                    enteredPassword.setError("test1231241")
                }
            }
        }
        /*FIx IT*/
        return true
    }


}