package se.ju.student.andoidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import se.ju.student.andoidproject.databinding.ActivityMainBinding

private lateinit var auth: FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener {

            val emailInput = findViewById<EditText>(R.id.email_input)
            val passwordInput = findViewById<EditText>(R.id.password_input)
            /*
            * ============================
            * Check The Entered Value with the firebase value
            * if True  open HomePageActivity
            *  else show Validation Error
            * */
            loginEmailAndPass(emailInput, passwordInput)

        }
    }
    /* Login With Email And Password */
    fun loginEmailAndPass(enteredEmail: EditText, enteredPass: EditText){
        if(enteredPass.editableText.toString().length < 6){
            enteredPass.error = "Wrong Password"
        }else {
            auth.signInWithEmailAndPassword(enteredEmail.editableText.toString(), enteredPass.editableText.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val intent = Intent(this, HomePageActivity::class.java)
                    startActivity(intent)
                } else {
                    checkEmailIsValid(enteredEmail)
                    Toast.makeText(baseContext, "The Email OR the Password Is Wrong",
                            Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /* Login with Facebook Account*/
    fun facebookLoginOnClick(view: View) {
        startActivity(Intent
            (this,HomePageActivity::class.java)
        )
    


        //Log.d("facebook", "Hi From Facebook")
    }
    /* Login With Google Account*/
    fun googleLoginOnClick(view: View) {
        Log.d("google", "Hi From Google")
    }
    /* Change Language To English */
    fun englishLanguageOnClick(view: View) {

    }

    /* Change Language To Swedish */
    fun swedenLanguageOnClick(view: View) {

    }
    /*Open Create Account Activity*/
    fun toCreateAccountActivityOnClick(view: View) {
        val btn = findViewById<Button>(R.id.new_user_button)

        btn.setOnClickListener {
            startActivity(
                Intent(this, CreateAccountActivity::class.java)
            )

        }


    }

    fun toForgetPasswordActivity(view: View) {


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
}