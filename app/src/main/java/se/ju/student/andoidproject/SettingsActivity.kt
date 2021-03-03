package se.ju.student.andoidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import java.util.*

class SettingsActivity : AppCompatActivity() {
    lateinit var locale: Locale
    private var currentLanguage = ""
    private var currentLang: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
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
                    SettingsActivity::class.java
            )
            refresh.putExtra(currentLang, localeName)
            startActivity(refresh)
        } else {
            Toast.makeText(
                    this@SettingsActivity, "Language, , already, , selected)!", Toast.LENGTH_SHORT).show();
        }
    }
}