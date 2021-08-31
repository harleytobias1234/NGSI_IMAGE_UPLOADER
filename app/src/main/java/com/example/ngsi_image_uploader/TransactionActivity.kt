package com.example.ngsi_image_uploader

import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.ngsi_image_uploader.data.Registration
import android.widget.*
import com.example.ngsi_image_uploader.utilities.ImageUtil
import android.content.*

import android.widget.Toast

import com.google.gson.JsonObject

import android.app.ProgressDialog
import android.text.TextUtils
import com.example.ngsi_image_uploader.Responses.POST_Registration
import com.example.ngsi_image_uploader.Responses.POST_UploadCheque
import com.example.ngsi_image_uploader.retrofit.adapter.Api_NGSI
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import java.util.*


class TransactionActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    var imageView : ImageView? = null
    var textView_base64: TextView? = null
    var base64Img_str: String? = "null"

    var et_transaction_number: EditText? = null
    var et_name: EditText? = null
    var et_email: EditText? = null
    var et_contact: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_transaction)

        //INIT
        et_transaction_number = findViewById(R.id.et_transaction_number)
        et_name = findViewById(R.id.et_name)
        et_email = findViewById(R.id.et_email)
        et_contact = findViewById(R.id.et_contact)
        imageView = findViewById(R.id.imageView)
        textView_base64 = findViewById(R.id.tv_binary64_image)
        ///

        // finding the button
        val btn_takephoto: Button = findViewById(R.id.btn_takephoto)!!
        val btn_save: Button = findViewById(R.id.btn_save)
        btn_takephoto.setOnClickListener{
            dispatchTakePictureIntent()
        }
        btn_save.setOnClickListener{
            checkerText(et_transaction_number?.text.toString(),
                et_name?.text.toString(),
                et_email?.text.toString(),
                et_contact?.text.toString(),
                base64Img_str.toString())
        }
    }

    private fun dispatchTakePictureIntent() {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            var base64String: String = ImageUtil.convertion_string(imageBitmap)
            var imageBase64: Bitmap = ImageUtil.convertion_bitmap(base64String)
            imageView?.setImageBitmap(imageBase64)
            base64Img_str = base64String
            var clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            var clip = ClipData.newPlainText("label", base64String)
            clipboard.setPrimaryClip(clip)
        }
    }
    //*********************************************************************************************************************
    //SQLITE Function
    private fun addRegister(transactionNum: String, name: String, email: String, contactNo: String, base64Img: String) {
        val dbHandler = dbHandler(this, "imageUploader.db", null, 1, null)
        val registration = Registration(transactionNum, name, email, base64Img, contactNo)
        Log.d("Registration", " " + registration.regTransactionNumber)
        dbHandler.addHandler(registration)
        Log.d("SaveDB", "Save complete to local db")
    }
    //*********************************************************************************************************************
    // API FUNCTION
    private fun uploadCheque(transactionNum: String, full_name: String, email: String, contactNo: String, base64Img: String) {
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
            paramObject.addProperty("cheque_num", transactionNum)
            paramObject.addProperty("cheque_image", base64Img)
            Api_NGSI.postUploadCheque()?.uploadCheque(paramObject)
                ?.enqueue(object : Callback<POST_UploadCheque?> {
                    override fun onResponse(
                        call: Call<POST_UploadCheque?>,
                        response: Response<POST_UploadCheque?>
                    ) {
                        if (response.isSuccessful()) {
                            val jsonObject = JSONObject(Gson().toJson(response.body()))
                            Log.d("login Status", "" + response.body().toString())
                            Toast.makeText(this@TransactionActivity, "" + jsonObject.getString("message"), Toast.LENGTH_LONG ).show()
                            progressDialog.dismiss()
                            addRegister(transactionNum, full_name, email, contactNo, base64Img)
                        } else {
                            try {
                                assert(response.errorBody() != null)
                                response.errorBody()?.let { Log.e("ERROR2", it.string()) }
                                Toast.makeText(this@TransactionActivity, "Check your input, User does not exists", Toast.LENGTH_LONG).show()
                                response.errorBody()?.let { Log.e("ERROR", it.string()) }
                                progressDialog.dismiss()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun onFailure(call: Call<POST_UploadCheque?>, t: Throwable) {
                        Log.d("response", Arrays.toString(t.stackTrace))
                        progressDialog.dismiss()
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //*************************************************************************************************************
    //Monitoring Functions
    private fun checkerText(transactionNum: String, name: String, email: String, contactNo: String, base64Img: String) {
        if(TextUtils.isEmpty(transactionNum)
            || TextUtils.isEmpty(name)
            || TextUtils.isEmpty(email)
            || TextUtils.isEmpty(contactNo)
            || base64Img == "null") {
            if (TextUtils.isEmpty(transactionNum)) { et_transaction_number?.setError("Please fill this") }
            if (TextUtils.isEmpty(name)) { et_name?.setError("Please fill this") }
            if (TextUtils.isEmpty(email)) { et_email?.setError("Please fill this") }
            if (TextUtils.isEmpty(contactNo)) { et_contact?.setError("Please fill this") }
            if (base64Img == "null") { Toast.makeText(this@TransactionActivity, "Please take a photo of the cheque", Toast.LENGTH_LONG).show() }
        } else { uploadCheque(transactionNum, name, email, contactNo, base64Img) }
    }
    //*************************************************************************************************************
}