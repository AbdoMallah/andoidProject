package se.ju.student.andoidproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import se.ju.student.andoidproject.databinding.FragmentImageToShowBinding


/**
 * A simple [Fragment] subclass.
 * Use the [ImageToShowFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImageToShowFragment : Fragment() {
    lateinit var binding: FragmentImageToShowBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )= FragmentImageToShowBinding.inflate(layoutInflater, container, false).run {
        binding = this
        root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        fun newInstance() = ImageToShowFragment()

    }
}