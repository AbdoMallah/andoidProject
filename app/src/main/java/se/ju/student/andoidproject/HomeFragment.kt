package se.ju.student.andoidproject

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment(R.layout.fragment_home) {

    val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view)

}
