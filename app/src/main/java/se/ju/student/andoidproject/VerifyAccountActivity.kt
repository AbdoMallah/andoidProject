package se.ju.student.andoidproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText

class VerifyAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_account)
        supportActionBar?.hide()
        val validationErrorArray = mutableListOf<String>() // To Add Any String to the array use --> validationErrorArray.add()
        val sendCodeButton = findViewById<Button>(R.id.send_code_button)
        val theVerificationCode = "12345"  //Implement it later, and get the generated code tht has been sent to the user
        sendCodeButton.setOnClickListener {
            val enteredCode = findViewById<EditText>(R.id.verify_code_input).editableText.toString()
            when {
                enteredCode.isEmpty() -> {
                    validationErrorArray.add(R.string.empty_field.toString())
                }
                enteredCode != theVerificationCode -> {
                    validationErrorArray.add("The Code Don't match the one you received")
                }
                else -> {
                    // No Errors Go To Login Page
                }
            }
        }
    }

    /* === Send a new code to the user === */
    fun resendVerificationCode(view: View) {}
}