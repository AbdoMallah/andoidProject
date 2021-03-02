package se.ju.student.andoidproject

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class CreateMemoryActivity : AppCompatActivity() {
    lateinit var filepath : Uri
    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_memory)
        supportActionBar?.hide()
        val cameraButton = findViewById<ImageButton>(R.id.camera_image_Button)
        cameraButton.setOnClickListener{
             startFileChooser()
        }
        val saveImage = findViewById<Button>(R.id.save_button)
        saveImage.setOnClickListener{
            uploadFile ()
        }
    }

    private fun uploadFile() {
        if(filepath != null ){
            var pd = ProgressDialog(this)
            pd.setTitle("uploading")
            pd.show()
            val imgageName = UUID.randomUUID().toString()
            var imageRef= FirebaseStorage.getInstance().reference.child("images/$imgageName")
            imageRef.putFile(filepath)
                    .addOnSuccessListener {po ->
                        pd.dismiss()
                        Toast.makeText(applicationContext,"File Uploaded",Toast.LENGTH_LONG).show()
                        saveImageInfoToFirebaseCloudFirestore()

                        imageRef.downloadUrl.addOnSuccessListener {
                            it.toString()
                            Log.d("UploadFile","fileLocation:$it")
                        }
                        setContentView(R.layout.activity_home_page)

                    }
                    .addOnFailureListener{po ->
                         pd.dismiss()
                         Toast.makeText(applicationContext,po.message,Toast.LENGTH_LONG).show()
                    }
                    .addOnProgressListener {po ->
                        var progress : Double =(10000.0 * po.bytesTransferred)/ po.totalByteCount
                        pd.setMessage("Uploaded ${progress.toInt()} % ")

                    }
        }
    }

    private fun saveImageInfoToFirebaseCloudFirestore() {
        // Create a new user with a first and last name
        val user: MutableMap<String, Any> = HashMap()
        user["first"] = "Ada"
        user["last"] = "Lovelace"
        user["born"] = 1815


// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener {
                    }
                .addOnFailureListener {

                }
    }

    private fun startFileChooser() {
        var intent = Intent()
        intent .setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Choose Picture"),111)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==111 && resultCode== Activity.RESULT_OK && data != null){
            filepath = data.data!!
        }
        var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filepath)
        val imageView = findViewById<ImageView>(R.id.image_View)

        imageView.setImageBitmap(bitmap)
    }
}