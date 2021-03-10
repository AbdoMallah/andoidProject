package se.ju.student.andoidproject

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


import com.squareup.picasso.Picasso
import se.ju.student.andoidproject.databinding.FragmentNewestMemoryBinding


class NewestMemoryFragment : Fragment() {
    class WatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var binding: FragmentNewestMemoryBinding
    private var storageRef = FirebaseStorage.getInstance().reference
    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser

    val currentUserId = currentUser?.uid



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ) =FragmentNewestMemoryBinding.inflate(inflater, container, false).run {
        binding = this



        val collectionReference: DocumentReference = db.collection("memorys").document("memory")
        val options : FirestoreRecyclerOptions<Memory> =
            FirestoreRecyclerOptions.Builder<Memory>().setQuery(collectionReference,Memory::class)
            .setLifecycleOwner(this@NewestMemoryFragment).build()

        val adapter = object: FirestoreRecyclerAdapter<Memory, WatchViewHolder>(options){

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchViewHolder {
                val view : View = LayoutInflater.from(parent.context).inflate(
                    R.layout.layout_single_row,
                    parent,
                    false
                )
                return WatchViewHolder(view)
            }

            override fun onBindViewHolder(holder: WatchViewHolder, position: Int, model: Memory) {

                val memoryTitle : TextView = holder.itemView.findViewById(R.id.fragment_title_text_view)
                val memoryImage : ImageView = holder.itemView.findViewById(R.id.fragment_image_container)
                val memoryDate : TextView = holder.itemView.findViewById(R.id.fragment_date_text_view)
                val progressBar: ProgressBar = holder.itemView.findViewById(R.id.progress_Bar)

                val imgReference = model.Img
                val pathReference = storageRef.child(imgReference as String)
                pathReference.downloadUrl.addOnSuccessListener{
                    progressBar.visibility = View.INVISIBLE
                    Picasso.get().load(it).into(memoryImage)
                }.addOnFailureListener{
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(activity,getString(R.string.download_Errors), Toast.LENGTH_SHORT).show()
                }

                memoryTitle.text = model.title
                memoryDate.text = model.date

                holder.itemView.setOnClickListener{
                    var intent = Intent(context, MemoryViewActivity::class.java).apply {
                        putExtra("newestId", model.id)
                    }
                    startActivity(intent)
                }
            }
        }
      /*  newestTitlesRecycleView.adapter = adapter
        newestTitlesRecycleView.layoutManager = LinearLayoutManager(context)
        adapter.startListening()

        add_button.setOnClickListener{
            var intent = Intent(context, CreateMemoryActivity::class.java).apply {
            }
            startActivity(intent)
        }
        root

*/
    }
}