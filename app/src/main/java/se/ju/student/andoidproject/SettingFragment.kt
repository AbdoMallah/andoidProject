package se.ju.student.andoidproject
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import se.ju.student.andoidproject.databinding.FragmentSettingBinding
import java.util.*

class SettingFragment : Fragment(R.layout.fragment_setting) {
    var counter = 0
    private lateinit var binding: FragmentSettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSettingBinding.inflate(inflater, container, false).run {
        binding = this
        root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



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
