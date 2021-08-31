package com.example.ngsi_image_uploader

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory.decodeFile
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.ngsi_image_uploader.Responses.POST_UploadCheque
import com.example.ngsi_image_uploader.data.Registration
import com.example.ngsi_image_uploader.retrofit.adapter.Api_NGSI
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.util.*
import android.media.ThumbnailUtils
import com.example.ngsi_image_uploader.utilities.ImageUtil


class ChequeActivity : AppCompatActivity() {
    //INIT CAMERA DATA
    var APP_TAG: String? = "ngsi_image_uploader"
    var photoFileName = "cheque1.jpg"
    var photoFileName2 = "depositslip1.jpg"
    var photoFile: File? = null
    var photoFile2: File? = null
    var takenImageA: Bitmap? = null
    var takenImageB: Bitmap? = null
    var REQUEST_ID_MULTIPLE_PERMISSIONS: Int? = 7;
    /////////////////////////////////
    //INIT
    var img_picker: String? = null

    val REQUEST_IMAGE_CAPTURE = 1
    var imageView: ImageView? = null
    var img_deposit: ImageView? = null
    var textView_base64: TextView? = null
    var base64Img_str: String? = null
    var base64Img_str2: String? = null

    var et_bank_number: EditText? = null
    var et_transaction_number: EditText? = null
    var et_name: EditText? = null
    var et_email: EditText? = null
    var et_contact: EditText? = null

    var Amount:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_cheque)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        checkAndroidVersion()
        //INIT
        et_bank_number = findViewById<EditText>(R.id.et_bank_number)
        et_transaction_number = findViewById<EditText>(R.id.et_transaction_number)
        et_name = findViewById<EditText>(R.id.et_name)
        et_email = findViewById<EditText>(R.id.et_email)
        et_contact = findViewById<EditText>(R.id.et_contact)
        imageView = findViewById(R.id.imageView)
        img_deposit = findViewById(R.id.img_deposit)
        textView_base64 = findViewById(R.id.tv_binary64_image)
        //Bundles
        val BankRefNumber:String = intent.getStringExtra("BankRefNumber").toString()
        val Email:String = intent.getStringExtra("Email").toString()
        Amount = intent.getStringExtra("Amount").toString()
        et_email?.setText(Email)
        et_bank_number?.setText(BankRefNumber)
        // finding the button
        val btn_takephoto = findViewById<Button>(R.id.btn_takephoto)
        val btn_takephoto2 = findViewById<Button>(R.id.btn_takephoto2)
        val btn_save = findViewById<Button>(R.id.btn_save)
        btn_takephoto.setOnClickListener{
            img_picker = "A"
            dispatchTakePictureIntent()
        }
        btn_takephoto2.setOnClickListener{
            img_picker = "B"
            dispatchTakePictureIntent()
        }
        btn_save.setOnClickListener{
            checkerText(et_transaction_number?.text.toString(),
            et_name?.text.toString(),
            et_email?.text.toString(),
            et_contact?.text.toString(),
            base64Img_str.toString(), base64Img_str2.toString())
        }
    }

    //** CAMERA FUNCTIONS *******************************************************************************************
    private fun dispatchTakePictureIntent() {
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            if(img_picker == "A") {
                photoFile = getPhotoFileUri(photoFileName);
                val fileProvider =
                    FileProvider.getUriForFile(this@ChequeActivity, "com.example.ngsi_image_uploader.provider", photoFile!!)
                i.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
                Log.d("CheckFileProvider", "" + fileProvider.toString())
                startActivityForResult(i, REQUEST_IMAGE_CAPTURE)
            }
            else if(img_picker == "B") {
                photoFile2 = getPhotoFileUri(photoFileName2);
                val fileProvider =
                    FileProvider.getUriForFile(this@ChequeActivity, "com.example.ngsi_image_uploader.provider", photoFile2!!)
                i.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
                Log.d("CheckFileProvider", "" + fileProvider.toString())
                startActivityForResult(i, REQUEST_IMAGE_CAPTURE)
            }


        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            Log.e("ErrorCamera", "" + e.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(img_picker == "A") {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//                val imageBitmap = data?.extras?.get("data") as Bitmap
//                val bytes = ByteArrayOutputStream()
//                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                var base64String: String = ImageUtil.convertion_string(decodeFile(photoFile!!.absolutePath))
//                imageView?.setImageBitmap(imageBitmap)
                base64Img_str = base64String
                val thumbImage =
                    ThumbnailUtils.extractThumbnail(decodeFile(photoFile!!.absolutePath), 720 , 1280 )
                imageView!!.setImageBitmap(thumbImage)
                img_picker = ""
            }
        }
        else if(img_picker == "B") {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//                val imageBitmap = data?.extras?.get("data") as Bitmap
//                val bytes = ByteArrayOutputStream()
//                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                var base64String2: String = ImageUtil.convertion_string(decodeFile(photoFile2!!.absolutePath))
//                img_deposit?.setImageBitmap(imageBitmap)
                base64Img_str2 = base64String2
//            var clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//            var clip = ClipData.newPlainText("label", base64String)
//            clipboard.setPrimaryClip(clip)
                val thumbImage =
                    ThumbnailUtils.extractThumbnail(decodeFile(photoFile2!!.absolutePath), 720 , 1280 )
                img_deposit?.setImageBitmap(thumbImage)
                img_picker = ""
            }
        }

    }
    //** END CAMERA FUNCTIONS ********************************************************************************************

    //SQLITE FUNCTION
    private fun addRegister(chequeNum: String, name: String, email: String, contactNo: String, base64Img: String) {
        val dbHandler = dbHandler(this, "imageUploader.db", null, 1, null)
        val registration = Registration(chequeNum, name, email, base64Img, contactNo)
        Log.d("Registration", " " + registration.regTransactionNumber)
        dbHandler.addHandler(registration)
        Log.d("SaveDB", "Save complete to local db")
    }
    //*********************************************************************************************************************
    // API FUNCTION
    private fun uploadCheque(chequeNum: String, full_name: String, email: String, contactNo: String, base64Img: String) {
        // display a progress dialog
        val progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false) // set cancelable to false
        progressDialog.setMessage("Please Wait") // set message
        progressDialog.show() // show progress dialog
        try {
            val paramObject = JsonObject()
            paramObject.addProperty("full_name", full_name)
            paramObject.addProperty("email", email)
            paramObject.addProperty("mobile_num", contactNo)
            paramObject.addProperty("bank_reference_number", et_bank_number?.text.toString())
            paramObject.addProperty("cheque_num", chequeNum)
            paramObject.addProperty("cheque_image", base64Img_str)
            paramObject.addProperty("deposit_image", base64Img_str2)
            paramObject.addProperty("amount", Amount)
            Api_NGSI.postUploadCheque()?.uploadCheque(paramObject)
                ?.enqueue(object : Callback<POST_UploadCheque?> {
                    override fun onResponse(
                        call: Call<POST_UploadCheque?>,
                        response: Response<POST_UploadCheque?>
                    ) {
                        if (response.isSuccessful) {
                            val jsonObject = JSONObject(Gson().toJson(response.body()))
                            Toast.makeText(this@ChequeActivity, "" + jsonObject.getString("message"), Toast.LENGTH_LONG ).show()
                            progressDialog.dismiss()
                            finish()
                        } else {
                            try {
                                assert(response.errorBody() != null)
                                response.errorBody()?.let { Log.e("ERROR2", it.string()) }
                                response.errorBody()?.let { Log.e("ERROR", it.string()) }
                                progressDialog.dismiss()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun onFailure(call: Call<POST_UploadCheque?>, t: Throwable) {
                        Log.d("response", Arrays.toString(t.stackTrace))
                        Toast.makeText(this@ChequeActivity, "Too long to Respond, please try again", Toast.LENGTH_LONG).show()
                        progressDialog.dismiss()
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    //*************************************************************************************************************
    //Monitoring Functions
    private fun checkerText(chequeNum: String, name: String, email: String, contactNo: String, base64Img: String, base64Img2: String) {
        if(TextUtils.isEmpty(chequeNum)
            || TextUtils.isEmpty(name)
            || TextUtils.isEmpty(email)
            || TextUtils.isEmpty(contactNo)
            || TextUtils.isEmpty(base64Img)
            || TextUtils.isEmpty(base64Img2)) {
            if (TextUtils.isEmpty(chequeNum)) { et_transaction_number?.setError("Please fill this") }
            if (TextUtils.isEmpty(name)) { et_name?.setError("Please fill this") }
            if (TextUtils.isEmpty(email)) { et_email?.setError("Please fill this") }
            if (TextUtils.isEmpty(contactNo)) { et_contact?.setError("Please fill this") }
            if (base64Img == "null") { Toast.makeText(this@ChequeActivity, "Please take a photo of the cheque", Toast.LENGTH_LONG).show() }
            if (base64Img2 == "null") { Toast.makeText(this@ChequeActivity, "Please take a photo of the deposit", Toast.LENGTH_LONG).show() }
        } else { uploadCheque(chequeNum, name, email, contactNo, base64Img) }
    }
    //*************************************************************************************************************
    fun getPhotoFileUri(fileName: String): File? {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d("NotCreated", "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    //****PERMISSIONS/////////////
    private fun checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions()
        } else {
            // code for lollipop and pre-lollipop devices
        }
    }
    private fun checkAndRequestPermissions(): Boolean {
        val camera = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        )
        val wtite = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val read = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this, listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS!!
            )
            return false
        }
        return true
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        Log.d("in fragment on request", "Permission callback called-------")
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                val perms: MutableMap<String, Int> = HashMap()
                // Initialize the map with both permissions
                perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] =
                    PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.READ_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                // Fill with actual results from user
                if (grantResults.size > 0) {
                    var i = 0
                    while (i < permissions.size) {
                        perms[permissions[i]] = grantResults[i]
                        i++
                    }
                    // Check for both permissions
                    if (perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED && perms[Manifest.permission.READ_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(
                            "in fragment on request",
                            "CAMERA & WRITE_EXTERNAL_STORAGE READ_EXTERNAL_STORAGE permission granted"
                        )
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(
                            "in fragment on request",
                            "Some permissions are not granted ask again "
                        )
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.CAMERA
                            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                        ) {
                            showDialogOK("Camera and Storage Permission required for this app",
                                DialogInterface.OnClickListener { dialog, which ->
                                    when (which) {
                                        DialogInterface.BUTTON_POSITIVE -> checkAndRequestPermissions()
                                        DialogInterface.BUTTON_NEGATIVE -> {
                                        }
                                    }
                                })
                        } else {
                            Toast.makeText(
                                this,
                                "Go to settings and enable permissions",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }
    }
    private fun showDialogOK(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", okListener)
            .create()
            .show()
    }
    //////////////////////////////
}