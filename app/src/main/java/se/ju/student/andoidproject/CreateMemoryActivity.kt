package se.ju.student.andoidproject

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class CreateMemoryActivity : AppCompatActivity() {
    lateinit var filepath : Uri
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
                        imageRef.downloadUrl.addOnSuccessListener {
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