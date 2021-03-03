package se.ju.student.andoidproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import se.ju.student.andoidproject.R.id.forget_password_text_view
import se.ju.student.andoidproject.R.id.start
import se.ju.student.andoidproject.databinding.ActivityMainBinding
import java.util.*

private lateinit var auth: FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var locale: Locale
    private var currentLanguage = ""
    private var currentLang: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener {
            val emailInput = findViewById<EditText>(R.id.email_input)
            val passwordInput = findViewById<EditText>(R.id.password_input)
            closeKeyboard(passwordInput)
            loginEmailAndPass(emailInput, passwordInput)
        }

        currentLanguage = intent.getStringExtra(currentLang).toString()

    }

    private fun closeKeyboard(view: View){
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    /* Login With Email And Password */
    private fun loginEmailAndPass(enteredEmail: EditText, enteredPass: EditText){
        if(enteredPass.editableText.toString().length < 6){
            enteredPass.error = "Wrong Password"
        }else {
            auth.signInWithEmailAndPassword(enteredEmail.editableText.toString(), enteredPass.editableText.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    if(auth.currentUser?.isEmailVerified == true){
                        startActivity(Intent(this, HomePageActivity::class.java))
                    }else{
                        Toast.makeText(baseContext, getString(R.string.email_not_valid), Toast.LENGTH_LONG).show()
                    }

                } else {
                    checkEmailIsValid(enteredEmail)
                    Toast.makeText(baseContext, getString(R.string.wrong_email_password),
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
        val btn = findViewById<ImageButton>(R.id.united_kingdom_icon)
        btn.setOnClickListener {
            Log.d("English", "Change Language to English")
            setLocale("en")
        }
    }

    /* Change Language To Swedish */
    fun swedenLanguageOnClick(view: View) {
        val btn = findViewById<ImageButton>(R.id.sweden_icon)
        btn.setOnClickListener {
            Log.d("Svenska", "Byt språk till svenska")
            setLocale("sv")
        }
    }

    /*
    * Change Language fun
    * setLocale(String)
    * */
    private fun setLocale(localeName: String) {
        if (localeName != currentLanguage) {
            locale = Locale(localeName)
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            res.updateConfiguration(conf, dm)
            val refresh = Intent(
                    this,
                    MainActivity::class.java
            )
            refresh.putExtra(currentLang, localeName)
            startActivity(refresh)
        } else {
            Toast.makeText(
                    this@MainActivity, "Language, , already, , selected)!", Toast.LENGTH_SHORT).show();
        }
    }
    /*Open Create Account Activity */
    fun toCreateAccountActivityOnClick(view: View) {
        val btn = findViewById<Button>(R.id.new_user_button)
        btn.setOnClickListener {
            Log.d("Create", "Hi from Create")
            startActivity(Intent(this, CreateAccountActivity::class.java))

        }

    }

    fun toForgetPasswordActivity(view: View) {
        val btn = findViewById<TextView>(forget_password_text_view)
        btn.setOnClickListener{
            Log.d("Forgot Password","Hi from forget password")
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
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
}