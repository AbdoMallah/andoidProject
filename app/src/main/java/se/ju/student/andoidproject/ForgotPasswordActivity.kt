package se.ju.student.andoidproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val resetPasswordBtn = findViewById<Button>(R.id.reset_password)
        auth = FirebaseAuth.getInstance()
        resetPasswordBtn.setOnClickListener {
            val emailInput = findViewById<EditText>(R.id.forget_password_activity_edit_text)
            if(checkEmailIsValid(emailInput)){
                auth.sendPasswordResetEmail(emailInput.editableText.toString()).addOnCompleteListener{
                    task->
                    if(task.isSuccessful){
                        Toast.makeText(baseContext, getString(R.string.forget_password_success), Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }

    }

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


