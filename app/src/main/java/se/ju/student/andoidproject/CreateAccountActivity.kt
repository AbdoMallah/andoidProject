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


        createButton.setOnClickListener {
            val firstNameInput = findViewById<EditText>(R.id.first_name_input)
            val lastNameInput = findViewById<EditText>(R.id.last_name_input)
            val emailInput = findViewById<EditText>(R.id.email_input)
            val passwordInput = findViewById<EditText>(R.id.password_input)
            val repeatPasswordInput = findViewById<EditText>(R.id.password_repeat_input)

            when {
                checkFirstnameIsValid(firstNameInput) && checkLastnameIsValid(lastNameInput)
                        && checkEmailIsValid(emailInput) && checkPasswordIsValid(passwordInput, repeatPasswordInput) -> {
                    Log.d("Validation", "Very Good")

                }
            }



            /*
            * ======================================
            * Add user info to Firebase
            * Send An Email To the User With a code.
            * ======================================
            *  */


            /*startActivity(
                    Intent(this, VerifyAccountActivity::class.java)
            )
            finish()
            */

        }

    }

    /*
    * Check email is valid
    * Boolean : True if email is valid and false if not
    * checkEmailIsValid(EditText)
    * */
    private fun checkEmailIsValid(emailInput: EditText): Boolean {
        val theEmail = emailInput.editableText.toString()
        var emailIsGood = false
        if (theEmail.isNotEmpty()) {
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(theEmail).matches())
                emailIsGood = true
            else{
                emailInput.error = getString(R.string.email_validation_error)
            }
        }else{
            emailInput.error = getString(R.string.empty_field_error)
        }
        return emailIsGood
    }
    /*
     * checkPassword: BOOLEAN.
     * Return true if the password is following the conditions.
     * checkPassword(EditText, EditText)
     * */
    private fun checkPasswordIsValid(passwordInput: EditText, repeatedPasswordInput: EditText): Boolean {
        val thePassword = passwordInput.editableText.toString()
        val repeatedPassword = repeatedPasswordInput.editableText.toString()
        var passwordIsGood = false
        when {
            thePassword.isEmpty() && repeatedPassword.isEmpty() -> {
                passwordInput.error = getString(R.string.empty_field_error)
                repeatedPasswordInput.error = getString(R.string.empty_field_error)
            }
            else -> {
                if (thePassword.length >= MINIMUM_PASSWORD_CHARACTERS) {
                    if (thePassword == repeatedPassword)
                        passwordIsGood = true
                }else{
                    passwordInput.error = getString(R.string.password_validation_error)
                }
            }
        }
        return passwordIsGood
    }
    /*
    * checkTheFirstName: BOOLEAN
    * Return true if the first name is following the conditions
    * checkTheFirstname(EditText)
    * */
    private fun checkFirstnameIsValid(firstnameInput: EditText): Boolean {
        var nameIsGood = false
        val theFirstname = firstnameInput.editableText.toString()
        when {
            theFirstname.isEmpty() -> {
                if (theFirstname.length < MINIMUM_FIRSTNAME_CHARACTERS || theFirstname.length > MAXIMUM_FIRSTNAME_CHARACTERS)
                    firstnameInput.error = getString(R.string.empty_field_error)
            }
            else -> {
                nameIsGood = true
            }
        }
        return nameIsGood
    }
    /*
    * checkTheLastName: BOOLEAN.
    * Return true if the last name is following the conditions.
    * checkTheLastName(EditText)
    * */
    private fun checkLastnameIsValid(lastnameInput: EditText): Boolean {
        var nameIsGood = false
        val theLastname = lastnameInput.editableText.toString()
        when {
            theLastname.isEmpty() -> {
                if (theLastname.length < MINIMUM_LASTNAME_CHARACTERS || theLastname.length > MAXIMUM_LASTNAME_CHARACTERS)
                    lastnameInput.error = getString(R.string.empty_field_error)
            }
            else -> {
                nameIsGood = true
            }
        }
        return nameIsGood
    }
}