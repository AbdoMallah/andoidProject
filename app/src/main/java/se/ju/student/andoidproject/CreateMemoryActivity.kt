package se.ju.student.andoidproject

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*

class CreateMemoryActivity : AppCompatActivity() {
    lateinit var filepath : Uri
    private lateinit var auth: FirebaseAuth
     companion object{
         private  const val CAMERA_PERMISSION_CODE = 1
         private const val CAMERA_REQUEST_CODE : Int = 2

     }
    var db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_memory)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        val cameraButton = findViewById<ImageButton>(R.id.camera_image_Button)
        val galleryButton = findViewById<ImageButton>(R.id.gallery_image_Button)

        cameraButton.setOnClickListener{
          if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
          }
        }
        galleryButton.setOnClickListener{
             startFileChooser()
        }
        val saveMemory = findViewById<Button>(R.id.save_button)
        saveMemory.setOnClickListener{
           uploadImage ()
            saveMemoryDetailsToFirebaseCloudFirestore()
        }
    }

   override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            }else{
                Toast.makeText(baseContext, getString(R.string.sign_up_success), Toast.LENGTH_LONG).show()
            }
        }
    }
   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("MSD", requestCode.toString())
        if (resultCode== Activity.RESULT_OK && requestCode == CAMERA_REQUEST_CODE && data != null ){

            val imageFCamera = data.extras!!.get("data") as Bitmap
            val imageView = findViewById<ImageView>(R.id.image_View)
            filepath = getImageUriFromBitmap(this, imageFCamera)
             imageView.setImageURI(filepath)
           // imageView.background.setVisibility(View.GONE)
            Log.d("Camera OnActivutyREsult", "IF call this function" + filepath.toString() + "this the filepath ")

        }
       if (requestCode==111 && resultCode== Activity.RESULT_OK && data != null){
           filepath = data.data!!
           var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filepath)
           val imageView = findViewById<ImageView>(R.id.image_View)
           imageView.setImageURI(filepath)
       }


    }
    private fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 1024, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }

   private fun uploadImage() {
        if(auth.currentUser == null){
            Toast.makeText(baseContext, getString(R.string.sign_in_error), Toast.LENGTH_LONG).show()
        }else{
            if(filepath != null ){
                var pd = ProgressDialog(this)
                pd.setTitle("uploading")
                pd.show()
                val userId = auth.currentUser.uid
                val imgageName = UUID.randomUUID().toString()
                var imageRef= FirebaseStorage.getInstance().reference.child("images/$userId/$imgageName")
                imageRef.putFile(filepath)
                        .addOnSuccessListener { po ->
                            pd.dismiss()
                            Toast.makeText(applicationContext, "File Uploaded", Toast.LENGTH_LONG).show()
                            imageRef.downloadUrl.addOnSuccessListener {
                                var imagePath = it.toString()
                                Log.d("UploadFile", "fileLocation:$it")
                            }
                            setContentView(R.layout.activity_home_page)

                        }
                        .addOnFailureListener{ to ->
                            pd.dismiss()
                            Toast.makeText(applicationContext, to.message, Toast.LENGTH_LONG).show()
                        }
                        .addOnProgressListener { to ->
                            val progress : Double =(1000.0 * to.bytesTransferred)/ to.totalByteCount
                            pd.setMessage("Uploaded ${progress.toInt()} % ")

                        }

              }
         }
    }

    private fun saveMemoryDetailsToFirebaseCloudFirestore() {
        val user = auth.currentUser
        val titleInput = findViewById<EditText>(R.id.title_input)
        val descriptionInput = findViewById<EditText>(R.id.description_input)
        val dateInput = findViewById<EditText>(R.id.date_input)
        val notificationInput = findViewById<EditText>(R.id.notification_input)
        val locationInput = findViewById<EditText>(R.id.location_input)
        val memoryId = UUID.randomUUID().toString()

        if (user == null) {
            Toast.makeText(baseContext, getString(R.string.sign_in_error), Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)

        }else{

            // Create a new memory
            val memory: MutableMap<String, Any> = HashMap()
            memory["Title"] = titleInput.editableText.toString()
            memory["Descripition"] = descriptionInput.editableText.toString()
            memory["Date"] = dateInput.editableText.toString()
            memory["Notifiction"] = notificationInput.editableText.toString()
            memory["Location"] = locationInput.editableText.toString()
            memory["MemoryId"] = memoryId

            // Add a new document with a generated ID
            db.collection("memorys").document("memory/${user.uid}/$memoryId")
                    .set(memory)
                    .addOnSuccessListener {
                        Log.d("Helllo", "DIt Works upploda to memory")
                    }
                    .addOnFailureListener {
                        Log.d("Helllo", "DIt Doesn't Works upploda to memory")
                    }
        }
    }
    private fun startFileChooser() {
        var intent = Intent()
        intent .setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Choose Picture"), 111)
    }

}