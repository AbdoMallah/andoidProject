package se.ju.student.andoidproject
import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import se.ju.student.andoidproject.databinding.FragmentSettingBinding
import java.util.*


class SettingFragment : Fragment(R.layout.fragment_setting) {
    var counter = 0
    private lateinit var binding: FragmentSettingBinding


    lateinit var locale: Locale
    private var currentLanguage = ""
    private var currentLang: String? = null

   /* override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       

    }*/

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) = FragmentSettingBinding.inflate(inflater, container, false).run {
        binding = this


        val view = LayoutInflater.from(context).inflate(R.layout.fragment_setting, null, false)
        binding = FragmentSettingBinding.inflate(layoutInflater)



         /* Change Language To Swedish */
         binding.swedenIcon.setOnClickListener {
             Log.d("Svenska", "Byt språk till svenska")
             setLocale("sv")
         }


         /* Change Language To English */
         binding.unitedKingdomIcon.setOnClickListener {
             Log.d("English", "Change language to English")
             setLocale("en")
         }

         binding.lightModeIcon.setOnClickListener {
             AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
             //delegate.applyDayNight()
         }

         binding.darkModeIcon.setOnClickListener {
             AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
             //delegate.applyDayNight()
         }

         root

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



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
                    context,
                    SettingFragment::class.java
            )
            refresh.putExtra(currentLang, localeName)
            startActivity(refresh)
        } else {
            Toast.makeText(context, "Language, , already, , selected)!", Toast.LENGTH_SHORT).show()
            }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(STATE_COUNTER, counter)
    }


    companion object {

        const val STATE_COUNTER = "STATE_COUNTER"
        const val ARG_START_VALUE = "ARG_START_VALUE"



    }

}
