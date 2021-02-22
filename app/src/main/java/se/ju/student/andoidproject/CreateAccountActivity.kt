package se.ju.student.andoidproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

const val MINIMUM_FIRSTNAME_CHARACTERS = 2
const val MAXIMUM_FIRSTNAME_CHARACTERS = 20
const val MINIMUM_LASTNAME_CHARACTERS = 2
const val MAXIMUM_LASTNAME_CHARACTERS = 20
const val MINIMUM_PASSWORD_CHARACTERS = 6
const val MAXIMUM_PASSWORD_CHARACTERS = 20
private lateinit var auth: FirebaseAuth

open class CreateAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()

        val createButton = findViewById<Button>(R.id.create_button)



        createButton.setOnClickListener {
            val firstNameInput = findViewById<EditText>(R.id.first_name_input)
            val lastNameInput = findViewById<EditText>(R.id.last_name_input)
            val emailInput = findViewById<EditText>(R.id.email_input)
            val passwordInput = findViewById<EditText>(R.id.password_input)
            val repeatPasswordInput = findViewById<EditText>(R.id.password_repeat_input)
            closeKeyboard(repeatPasswordInput)
            if(checkFirstnameIsValid(firstNameInput) && checkLastnameIsValid(lastNameInput) && checkEmailIsValid(emailInput) && checkPasswordIsValid(passwordInput, repeatPasswordInput)){
                auth.createUserWithEmailAndPassword(emailInput.editableText.toString(), passwordInput.editableText.toString()).addOnCompleteListener{
                    task ->
                    if (task.isSuccessful) {
                        if (auth.currentUser != null) {
                            auth.currentUser?.sendEmailVerification()?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val user = auth.currentUser
                                    Toast.makeText(baseContext, getString(R.string.sign_up_success), Toast.LENGTH_LONG).show()
                                    startActivity(Intent(this, MainActivity::class.java))
                                    finish()
                                }
                            }
                        }
                    }else {
                        Toast.makeText(baseContext, getString(R.string.sign_up_error), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


    /*
    * Close keyboard
    * void closeKeyboard(View)
    * */
    private fun closeKeyboard(view: View){
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
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
                    else
                        passwordInput.error = getString(R.string.password_match_error)
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
                firstnameInput.error = getString(R.string.empty_field_error)
            }
            else -> {
                if (theFirstname.length < MINIMUM_FIRSTNAME_CHARACTERS || theFirstname.length > MAXIMUM_FIRSTNAME_CHARACTERS)
                    firstnameInput.error = getString(R.string.firstname_validation_error)
                else nameIsGood = true
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
                lastnameInput.error = getString(R.string.empty_field_error)
            }
            else -> {
                if (theLastname.length < MINIMUM_LASTNAME_CHARACTERS || theLastname.length > MAXIMUM_LASTNAME_CHARACTERS)
                    lastnameInput.error = getString(R.string.lastname_validation_error)
                else nameIsGood = true
            }
        }
        return nameIsGood
    }
    /*
    * Send A E-mail With the Verification Code to the user
    * */
    private fun sendVerificationCodeToUser(enteredEmail: String){

    }
}