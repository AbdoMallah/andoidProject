package se.ju.student.andoidproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.view.View

class HomePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
    }

 fun toCreateMemoryActivityOnClick(view: View){
     val btn = findViewById<Button>(R.id.add_button)
     btn.setOnClickListener{
         startActivity(Intent
             (this,CreateMemoryActivity::class.java)
         )
     }
 }
    fun toSettingsActivityOnClick(view: View){
        val btn = findViewById<Button>(R.id.setting_button)
        btn.setOnClickListener{
            startActivity(Intent
                (this,SettingsActivity::class.java)
            )
        }
    }

fun toHomePageActivityOnClick (view: View){
    val btn = findViewById<Button>(R.id.home_button)
    btn.setOnClickListener{
        startActivity(Intent
            (this,HomePageActivity::class.java)
        )
     }

    }

}
