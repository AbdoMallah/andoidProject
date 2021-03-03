package se.ju.student.andoidproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import se.ju.student.andoidproject.R.id.forget_password_text_view
import se.ju.student.andoidproject.R.id.start
import se.ju.student.andoidproject.databinding.ActivityMainBinding
import java.util.*

private lateinit var auth: FirebaseAuth
lateinit var mGoogleSignInClient: GoogleSignInClient
val RC_SIGN_IN = 123

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
    override fun onStart() {
        super.onStart()
        if(GoogleSignIn.getLastSignedInAccount(this)!=null){
            startActivity(Intent(this, HomePageActivity::class.java))
            finish()
        }
    }

    fun googleLoginOnClick(view: View){
        createRequest()
        signIn()
    }
    /* Login With Google Account*/
    private fun createRequest() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d("Success", "signInWithCredential:success")
                        val user = auth.currentUser
                        startActivity(Intent(this, HomePageActivity::class.java).putExtra("user", user))
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Dailed", "signInWithCredential:failure", task.exception)
                    }
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("Success", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("Failed", "Google sign in failed", e)
            }
        }
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