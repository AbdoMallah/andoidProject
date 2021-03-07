package se.ju.student.andoidproject

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*



class CreateMemoryActivity : AppCompatActivity() {
    lateinit var filepath : Uri
    private lateinit var auth: FirebaseAuth
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest

    var db = FirebaseFirestore.getInstance()

     companion object{
         private  const val CAMERA_PERMISSION_CODE = 1
         private const val CAMERA_REQUEST_CODE : Int = 2
         private const val PERMISSION_ID =200
     }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_memory)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        val cameraButton = findViewById<ImageButton>(R.id.camera_image_Button)
        val galleryButton = findViewById<ImageButton>(R.id.gallery_image_Button)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val dateButton = findViewById<Button>(R.id.date_Button)
        val locationButton = findViewById<Button>(R.id.location_Button)

        locationButton.setOnClickListener{
            Log.d("Debug:", checkPermission().toString())
            Log.d("Debug:", isLocationEnabled().toString())
            requestPermission()
            /* fusedLocationProviderClient.lastLocation.addOnSuccessListener{location: Location? ->
                 textView.text = location?.latitude.toString() + "," + location?.longitude.toString()
             }*/
            getLastLocation()
        }


        dateButton.setOnClickListener {
            showDatePickerDialog()
        }
        cameraButton.setOnClickListener{
          if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
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

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager,"datePicker")
    }
    fun onDateSelected(day:Int, month : Int,year :Int){
        val dateTextView = findViewById<TextView>(R.id.date_input)
        dateTextView.setText("$day,$month,$year")

    }

    @SuppressLint("MissingPermission")
    fun getLastLocation(){
        if(checkPermission()){
            if(isLocationEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task->
                    var location:Location? = task.result
                    if(location == null){
                        NewLocationData()
                    }else{
                        Log.d("Debug:", "Your Location:" + location.longitude + location.latitude)
                        val latitude_input= location.latitude
                        val longitude_input = location.longitude
                        val location = findViewById<TextView>(R.id.location_input)
                        location.text =  getCityName(latitude_input, longitude_input)
                    }
                }
            }else{
                Toast.makeText(this, "Please Turn on Your device Location", Toast.LENGTH_SHORT).show()
            }
        }else{
            requestPermission()
        }
    }

    fun NewLocationData(){
        var locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient!!.requestLocationUpdates(
                locationRequest, locationCallback, Looper.myLooper()
            )
            return
        }

    }



    private val locationCallback = object : LocationCallback(){
        @SuppressLint("SetTextI18n")
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
            Log.d("Debug:", "your last last location: " + lastLocation.longitude.toString())
            val location = findViewById<TextView>(R.id.location_input)
            location.text = "You Last Location is : Long: "+ lastLocation.longitude + " , Lat: " + lastLocation.latitude + "\n" + getCityName(
                lastLocation.latitude,
                lastLocation.longitude
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            }else{
                Toast.makeText(baseContext, getString(R.string.sign_up_success), Toast.LENGTH_LONG).show()
            }
        }
        if(requestCode == PERMISSION_ID){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Debug:", "You have the Permission For GPS")
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
            Log.d(
                "Camera OnActivutyREsult",
                "IF call this function" + filepath.toString() + "this the filepath "
            )

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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            bitmap,
            "Title",
            null
        )
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
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Choose Picture"), 111)
    }



    private fun checkPermission():Boolean{
        //this function will return a boolean
        //true: if we have permission
        //false if not
        if(
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }

        return false

    }
    private fun requestPermission(){
        //this function will allows us to tell the user to requesut the necessary permsiion if they are not garented
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }
    private fun isLocationEnabled():Boolean{
        //this function will return to us the state of the location service
        //if the gps or the network provider is enabled then it will return true otherwise it will return false
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @Throws(IOException::class)
    private fun getCityNameByCoordinates(lat: Double, lon: Double): String? {
        var geoCoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address> = geoCoder.getFromLocation(lat, lon, 10)
        if (addresses != null && addresses.isNotEmpty()) {
            for (adr in addresses) {
                if (adr.locality != null && adr.locality.isNotEmpty()) {
                    return adr.locality
                }
            }
        }
        return null
    }



    private fun getCityName(lat: Double, long: Double):String {
        var cityName: String = ""
        var countryName: String = ""
        var geoCoder = Geocoder(this, Locale.getDefault())
        var adress = geoCoder.getFromLocation(lat, long, 3)
        cityName = getCityNameByCoordinates(lat,long).toString()
        countryName = adress[0].countryName
        Log.d(
            "Debug:",
            """Your City": $cityName ; your Country ${adress.get(0).countryName}"""
        )

        return cityName + " , " +countryName
    }
}

